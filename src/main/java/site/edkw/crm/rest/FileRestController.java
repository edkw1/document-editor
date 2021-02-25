package site.edkw.crm.rest;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.domain.File;
import site.edkw.crm.domain.Views;
import site.edkw.crm.service.FileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/files")
public class FileRestController {
    private final FileService fileService;

    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }


    @JsonView(Views.File.class)
    @GetMapping
    public List<File> getAllFiles(){
        return fileService.getAvailableFilesList();
    }

    @GetMapping("{id}")
    public File getFileInfo(@PathVariable("id") long id) throws FileNotFoundException {
        return fileService.getFileInfo(id);
    }


    @GetMapping("{id}/download")
    public ResponseEntity<Resource> getFile(@PathVariable("id") long id) throws FileNotFoundException {
        File file = fileService.getFileInfo(id);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(file.getName(), StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.set(HttpHeaders.CONTENT_TYPE, file.getType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file.getPath()));
    }


    @JsonView(Views.File.class)
    @PostMapping
    public File uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.storeFile(file);
    }
}
