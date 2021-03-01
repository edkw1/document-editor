package site.edkw.crm.service;

import org.springframework.web.multipart.MultipartFile;
import site.edkw.crm.domain.FileInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileInfoService {
    FileInfo storeFile(MultipartFile file) throws IOException;
    List<FileInfo> getAvailableFilesList();
    FileInfo getFileInfo(long id) throws FileNotFoundException;
    FileInfo getFileInfoByEditorKey(String key) throws FileNotFoundException;
    void deleteFile(long id) throws FileNotFoundException;
    void saveFileInfo(FileInfo fileInfo);
}
