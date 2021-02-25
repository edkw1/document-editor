package site.edkw.crm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.domain.File;
import site.edkw.crm.domain.Status;
import site.edkw.crm.domain.User;
import site.edkw.crm.repository.FileRepository;
import site.edkw.crm.security.jwt.JwtUser;
import site.edkw.crm.service.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final String SAVE_DIR;
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository,
                           @Value("${site.edkw.save-dir}") String dir) {
        this.fileRepository = fileRepository;
        SAVE_DIR = dir;
    }

    @Override
    public File storeFile(MultipartFile file) throws IOException {
        String username = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        File fileToDb = new File();
        fileToDb.setName(fileName);

        String uuid = UUID.randomUUID().toString();
        String fileSavePath = SAVE_DIR + java.io.File.separator + uuid + "." + fileName;
        System.out.println(fileSavePath);
        java.io.File f = new java.io.File("." + java.io.File.separator + fileSavePath);
        file.transferTo(f.getAbsoluteFile());

        fileToDb.setPath(fileSavePath);
        fileToDb.setStatus(Status.ACTIVE);
        fileToDb.setType(file.getContentType());
        fileToDb.setSize(file.getSize() / 1024);

        File savedFile = fileRepository.save(fileToDb);
        log.info("IN storeFile saved uploaded file {} by user {}", fileSavePath, username);
        return savedFile;
    }

    @Override
    public List<File> getAvailableFilesList() {
        return fileRepository.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public File getFileInfo(long id) throws FileNotFoundException {
        return fileRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }


    public void deleteFile(long id) throws FileNotFoundException{
        String username = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        log.warn("IN deleteFile user {} try to remove file {}", username, id);

        File file = fileRepository.findById(id).orElseThrow(FileNotFoundException::new);
        if(file.getStatus().equals(Status.DELETED)){
            throw new FileNotFoundException();
        }

        file.setStatus(Status.DELETED);
        fileRepository.save(file);
        log.info("IN deleteFile removed file {} ({}) by user {}", file.getPath(), file.getId(), username);
    }
}
