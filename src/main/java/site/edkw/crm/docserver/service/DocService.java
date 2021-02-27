package site.edkw.crm.docserver.service;

import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.dto.CallbackResponse;

public interface DocService {
    CallbackResponse processCallback(CallbackRequest request);
}
