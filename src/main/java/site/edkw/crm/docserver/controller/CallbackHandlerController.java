package site.edkw.crm.docserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.edkw.crm.docserver.domain.CallbackRequest;
import site.edkw.crm.docserver.dto.CallbackResponse;
import site.edkw.crm.docserver.service.DocService;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/docserver")
public class CallbackHandlerController {
    private final DocService docService;

    public CallbackHandlerController(DocService docService) {
        this.docService = docService;
    }

    @PostMapping
    public CallbackResponse callbackHandler(@RequestBody HashMap<String, Object> data) throws IOException {
        CallbackRequest callbackRequest = null;
        ObjectMapper mapper = new ObjectMapper();

        String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        System.out.println(str);
        return docService.processCallback(callbackRequest);
    }

}
