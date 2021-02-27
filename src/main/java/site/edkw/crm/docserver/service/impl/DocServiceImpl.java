package site.edkw.crm.docserver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.domain.Status;
import site.edkw.crm.docserver.dto.CallbackResponse;
import site.edkw.crm.docserver.service.DocService;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.service.FileInfoService;

import java.io.FileNotFoundException;
import java.util.UUID;

@Slf4j
@Service
public class DocServiceImpl implements DocService {
    private final FileInfoService fileInfoService;

    public DocServiceImpl(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    @Override
    public CallbackResponse processCallback(CallbackRequest callbackRequest) {
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

        return new CallbackResponse(0);
    }


    public DocConfig getConfig(long id) throws FileNotFoundException {
        FileInfo fileInfo = fileInfoService.getFileInfo(id);
        if(fileInfo.getEditorKey() == null){
            String generatedKey = generateEditorKey(id);
            fileInfo.setEditorKey(generatedKey);
            fileInfoService.saveFileInfo(fileInfo);
        }
        return new DocConfig();
    }

    private String generateEditorKey(long id) {
        return UUID.randomUUID().toString()+id;
    }


}
