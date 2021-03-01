package site.edkw.crm.docserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.edkw.crm.docserver.UnsupportedFileException;
import site.edkw.crm.docserver.domain.Action;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.domain.Status;
import site.edkw.crm.docserver.domain.config.Document;
import site.edkw.crm.docserver.domain.config.EditorConfig;
import site.edkw.crm.docserver.dto.CallbackResponse;
import site.edkw.crm.docserver.service.DocService;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.security.jwt.JwtTokenProvider;
import site.edkw.crm.security.jwt.JwtUser;
import site.edkw.crm.service.FileInfoService;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
public class DocServiceImpl implements DocService {
    private final FileInfoService fileInfoService;
    private final JwtTokenProvider tokenProvider;

    public DocServiceImpl(FileInfoService fileInfoService,
                          JwtTokenProvider tokenProvider) {
        this.fileInfoService = fileInfoService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public CallbackResponse processCallback(CallbackRequest callbackRequest) throws IOException {
        switch (callbackRequest.getStatus()) {
            case Status.BEING_EDITED -> {
                log.info("Document editing: ");
                printActions(callbackRequest.getActions());
            }
            case Status.CLOSED_WITHOUT_CHANGES -> {
                log.info("Document closed without changes");
                printActions(callbackRequest.getActions());
            }
            case Status.READY_FOR_SAVING -> {
                log.info("Document ready for saving");
                saveEditedDocument(callbackRequest, true);
                printActions(callbackRequest.getActions());
            }
            case Status.SAVING_ERROR -> {
                log.warn("Document saving error");
            }
            case Status.FORCE_SAVING -> {
                log.info("Document force saving");
                saveEditedDocument(callbackRequest, false);
            }
            case Status.ERROR_FORCE_SAVING -> {
                log.warn("Document error force saving");
            }
            default -> log.warn("Incorrect status");
        }

        return new CallbackResponse(0);
    }

    private void printActions(Action[] actions) {
        for (Action action : actions) {
            log.info("User {}({}) is {}",
                    getCurrentUser().getUsername(),
                    action.getUserId(),
                    action.getType() == Action.TYPE_USER_CONNECTED ? "connected" : "disconnected");
        }
    }

    private void saveEditedDocument(CallbackRequest callbackRequest, boolean clearEditorKey) throws IOException {
        JwtUser user = getCurrentUser();
        String editorKey = callbackRequest.getKey();
        FileInfo fileInfo = fileInfoService.getFileInfoByEditorKey(editorKey);

        URL url = new URL(callbackRequest.getUrl());
        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
        InputStream stream = connection.getInputStream();

        File savedFile = new File(fileInfo.getPath());
        try (FileOutputStream out = new FileOutputStream(savedFile)) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = stream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.flush();
        }

        if(clearEditorKey){
            fileInfo.setEditorKey(null);
            fileInfoService.saveFileInfo(fileInfo);
            log.info("IN saveEditedDocument in file {} removed editor key {} by user {}",
                    fileInfo.getPath(), editorKey, user.getUsername());
        }

        log.info("IN saveEditedDocument file \"{}\" was edited by user {}",
                fileInfo.getPath(), user.getUsername());
    }

    @Override
    public DocConfig createConfigForEditingDocument(long id, String mode,
                                                    HttpServletRequest request)
            throws FileNotFoundException, UnsupportedFileException {

        String token = tokenProvider.resolveToken(request);
        String hostAddr = "http://" + request.getRemoteAddr() + ":" + request.getServerPort();
        JwtUser user = getCurrentUser();
        FileInfo fileInfo = fileInfoService.getFileInfo(id);
        String fileName = fileInfo.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        DocConfig config = new DocConfig();
        Document document = new Document();
        document.setFileType(fileExtension);
        document.setTitle(fileName);
        document.setUrl(getFileUrlById(id, hostAddr, token));

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
                getCallbackUrl(hostAddr, token),
                user.getId(),
                user.getFirstname() + " " + user.getLastname(),
                null
        ));
        config.setDocumentType(getDocumentTypeByFileExtension(fileExtension));

        return config;
    }

    private String getCallbackUrl(String hostAddr, String token) {
        return hostAddr + "/api/v1/docserver?key=" + token;
    }

    private String getFileUrlById(long id, String hostAddr, String token) {
        return hostAddr + "/api/v1/files/" + id + "/download?key=" + token;
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


    private JwtUser getCurrentUser(){
        return (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
