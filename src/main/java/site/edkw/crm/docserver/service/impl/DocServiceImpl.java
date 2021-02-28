package site.edkw.crm.docserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.edkw.crm.docserver.UnsupportedFileException;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.domain.Status;
import site.edkw.crm.docserver.domain.config.Document;
import site.edkw.crm.docserver.domain.config.EditorConfig;
import site.edkw.crm.docserver.dto.CallbackResponse;
import site.edkw.crm.docserver.service.DocService;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.security.jwt.JwtUser;
import site.edkw.crm.service.FileInfoService;

import java.io.FileNotFoundException;
import java.util.UUID;

@Slf4j
@Service
public class DocServiceImpl implements DocService {
    private final FileInfoService fileInfoService;
    private static String HOST_URL;

    public DocServiceImpl(FileInfoService fileInfoService,
                          @Value("${site.edkw.host-url}") String hostUrl) {
        this.fileInfoService = fileInfoService;
        HOST_URL = hostUrl;
    }

    @Override
    public CallbackResponse processCallback(CallbackRequest callbackRequest) {
        if (callbackRequest != null) {
            switch (callbackRequest.getStatus()) {
                case Status.BEING_EDITED:
                    log.info("Document opened");

                    break;
                case Status.CLOSED_WITH_NO_CHANGES:
                    break;
                case Status.READY_FOR_SAVING:
                    break;
                case Status.SAVING_ERROR:
                    break;
                case Status.FORCE_SAVING:
                    break;
                case Status.ERROR_FORCE_SAVING:
                    break;
                default:
            }
        }

        return new CallbackResponse(0);
    }

    @Override
    public DocConfig createConfigForEditingDocument(long id, String mode, String token) throws FileNotFoundException, UnsupportedFileException {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FileInfo fileInfo = fileInfoService.getFileInfo(id);
        String fileName = fileInfo.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        DocConfig config = new DocConfig();
        Document document = new Document();
        document.setFileType(fileExtension);
        document.setTitle(fileName);
        document.setUrl(getFileUrlById(id, token));

        if (fileInfo.getEditorKey() == null) {
            String generatedKey = generateEditorKey(id);
            fileInfo.setEditorKey(generatedKey);
            fileInfoService.saveFileInfo(fileInfo);
            document.setKey(generatedKey);
        } else {
            document.setKey(fileInfo.getEditorKey());
        }

        config.setDocument(document);
        config.setEditorConfig(new EditorConfig(
                mode,
                getCallbackUrl(token),
                user.getId(),
                user.getFirstname() + " " + user.getLastname(),
                null
        ));
        config.setDocumentType(getDocumentTypeByFileExtension(fileExtension));

        return config;
    }

    private String getCallbackUrl(String token) {
        return HOST_URL + "/api/v1/docserver?key="+token;
    }

    private String getFileUrlById(long id, String token) {
        return HOST_URL + "/api/v1/files/" + id + "/download?key="+token;
    }


    private String getDocumentTypeByFileExtension(String fileExt) throws UnsupportedFileException {
        return switch (fileExt) {
            case "doc", "docm", "docx", "dot", "dotm", "dotx", "epub", "fodt",
                    "htm", "html", "mht", "odt", "ott", "pdf", "rtf", "txt", "djvu", "xps" -> "word";

            case "csv", "fods", "ods", "ots", "xls", "xlsm", "xlsx", "xlt", "xltm", "xltx" -> "cell";

            case "fodp", "odp", "otp", "pot", "potm", "potx", "pps", "ppsm", "ppsx", "ppt",
                    "pptm", "pptx" -> "slide";

            default -> throw new UnsupportedFileException("File not supported!");
        };
    }

    private String generateEditorKey(long id) {
        return UUID.randomUUID().toString() + id;
    }


}
