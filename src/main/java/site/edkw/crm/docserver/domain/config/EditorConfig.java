package site.edkw.crm.docserver.domain.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import site.edkw.crm.docserver.domain.User;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditorConfig {
    private String mode;
    private String lang = "ru";
    private String callbackUrl;
    private User user;
    private EditorCustomization customization = new EditorCustomization();
}
