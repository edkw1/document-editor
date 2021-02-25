package site.edkw.crm.service;

import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.domain.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    File storeFile(MultipartFile file) throws IOException;
    List<File> getAvailableFilesList();
    File getFileInfo(long id) throws FileNotFoundException;
    void deleteFile(long id) throws FileNotFoundException;
}
