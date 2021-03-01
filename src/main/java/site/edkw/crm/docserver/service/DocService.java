package site.edkw.crm.docserver.service;

import site.edkw.crm.docserver.UnsupportedFileException;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.domain.config.DocConfig;
import site.edkw.crm.docserver.dto.CallbackResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DocService {
    CallbackResponse processCallback(CallbackRequest request) throws IOException;
    DocConfig createConfigForEditingDocument(long id, String mode, HttpServletRequest request) throws FileNotFoundException, UnsupportedFileException;
}
