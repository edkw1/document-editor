package site.edkw.crm.docserver.service;

import site.edkw.crm.docserver.UnsupportedFileException;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.dto.CallbackResponse;

import java.io.FileNotFoundException;

public interface DocService {
    CallbackResponse processCallback(CallbackRequest request);
    DocConfig createConfigForEditingDocument(long id, String mode, String token) throws FileNotFoundException, UnsupportedFileException;
}
