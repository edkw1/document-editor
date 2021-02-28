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
    private EditorCustomization customization;

    public EditorConfig(String mode,
                        String callbackUrl,
                        long userId,
                        String username,
                        EditorCustomization customization){
        this.mode = mode;
        this.callbackUrl = callbackUrl;

        User user = new User();
        user.setId(String.valueOf(userId));
        user.setName(username);
        this.user = user;

        if(customization == null){
            this.customization = new EditorCustomization();
        }else{
            this.customization = customization;
        }

    }
}
