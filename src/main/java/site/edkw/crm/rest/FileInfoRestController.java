package site.edkw.crm.rest;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.docserver.UnsupportedFileException;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.service.DocService;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.domain.Views;
import site.edkw.crm.security.jwt.JwtTokenProvider;
import site.edkw.crm.service.FileInfoService;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/files")
public class FileInfoRestController {
    private final FileInfoService fileInfoService;
    private final DocService docService;
    private final JwtTokenProvider tokenProvider;

    public FileInfoRestController(FileInfoService fileInfoService,
                                  DocService docService,
                                  JwtTokenProvider tokenProvider) {
        this.fileInfoService = fileInfoService;
        this.docService = docService;
        this.tokenProvider = tokenProvider;
    }


    @JsonView(Views.File.class)
    @GetMapping
    public List<FileInfo> getAllFiles(){
        return fileInfoService.getAvailableFilesList();
    }

    @JsonView(Views.File.class)
    @GetMapping("{id}")
    public FileInfo getFileInfo(@PathVariable("id") long id) throws FileNotFoundException {
        return fileInfoService.getFileInfo(id);
    }


    @GetMapping("{id}/download")
    public ResponseEntity<Resource> getFile(@PathVariable("id") long id) throws FileNotFoundException {
        FileInfo fileInfo = fileInfoService.getFileInfo(id);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileInfo.getName(), StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.set(HttpHeaders.CONTENT_TYPE, fileInfo.getType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(fileInfo.getPath()));
    }

    @GetMapping("{id}/edit")
    public DocConfig getConfigForEditingFile(@PathVariable("id") long id,
                                             HttpServletRequest request)
            throws FileNotFoundException, UnsupportedFileException {
        return docService.createConfigForEditingDocument(id, "edit", tokenProvider.resolveToken(request));
    }

    @GetMapping("{id}/view")
    public DocConfig getConfigForViewingFile(@PathVariable("id") long id,
                                             HttpServletRequest request)
            throws FileNotFoundException, UnsupportedFileException {

        return docService.createConfigForEditingDocument(id, "view", tokenProvider.resolveToken(request));
    }


    @JsonView(Views.File.class)
    @PostMapping
    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileInfoService.storeFile(file);
    }

    @DeleteMapping("{id}")
    public void deleteFile(@PathVariable("id") long id) throws FileNotFoundException {
        fileInfoService.deleteFile(id);
    }
}
