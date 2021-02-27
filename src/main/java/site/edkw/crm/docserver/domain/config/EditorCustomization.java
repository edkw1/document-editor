package site.edkw.crm.docserver.domain.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EditorCustomization {
    private boolean autosave = true;
    private boolean forcesave = true;
    private boolean compactHeader = false;
    private boolean macros = false;
    private boolean plugins = false;
    private String unit = "cm";
    private boolean comments = false;
    private boolean chat = false;
    private boolean feedback = false;
}
