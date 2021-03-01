package site.edkw.crm.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.domain.Status;
import site.edkw.crm.repository.FileInfoRepository;
import site.edkw.crm.security.jwt.JwtUser;
import site.edkw.crm.service.FileInfoService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {
    private final String SAVE_DIR;
    private final FileInfoRepository fileInfoRepository;

    public FileInfoServiceImpl(FileInfoRepository fileInfoRepository,
                               @Value("${site.edkw.save-dir}") String dir) {
        this.fileInfoRepository = fileInfoRepository;
        SAVE_DIR = dir;
    }

    @Override
    public FileInfo storeFile(MultipartFile file) throws IOException {
        String username = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        FileInfo fileInfoToDb = new FileInfo();
        fileInfoToDb.setName(fileName);

        String uuid = UUID.randomUUID().toString();
        String fileSavePath = SAVE_DIR + java.io.File.separator + uuid + "." + fileName;
        System.out.println(fileSavePath);
        java.io.File f = new java.io.File("." + java.io.File.separator + fileSavePath);
        file.transferTo(f.getAbsoluteFile());

        fileInfoToDb.setPath(fileSavePath);
        fileInfoToDb.setStatus(Status.ACTIVE);
        fileInfoToDb.setType(file.getContentType());
        fileInfoToDb.setSize(file.getSize() / 1024);

        FileInfo savedFileInfo = fileInfoRepository.save(fileInfoToDb);
        log.info("IN storeFile saved uploaded file {} by user {}", fileSavePath, username);
        return savedFileInfo;
    }

    @Override
    public List<FileInfo> getAvailableFilesList() {
        return fileInfoRepository.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public FileInfo getFileInfo(long id) throws FileNotFoundException {
        return fileInfoRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }

    @Override
    public FileInfo getFileInfoByEditorKey(String key) throws FileNotFoundException {
        return fileInfoRepository.findByEditorKey(key)
                .orElseThrow(() -> new FileNotFoundException("File with same key (" + key + ") not found"));
    }


    public void deleteFile(long id) throws FileNotFoundException {
        String username = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        log.warn("IN deleteFile user {} try to remove file {}", username, id);

        FileInfo fileInfo = fileInfoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        if (fileInfo.getStatus().equals(Status.DELETED)) {
            throw new FileNotFoundException();
        }

        fileInfo.setStatus(Status.DELETED);
        fileInfoRepository.save(fileInfo);
        log.info("IN deleteFile removed file {} ({}) by user {}", fileInfo.getPath(), fileInfo.getId(), username);
    }

    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        fileInfoRepository.save(fileInfo);
    }
}
