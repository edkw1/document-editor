package site.edkw.crm.docserver.domain.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocConfig {
    private Document document;
    private String documentType;
    private String width = "100%";
    private String height = "97%";
    private EditorConfig editorConfig = new EditorConfig();
}
