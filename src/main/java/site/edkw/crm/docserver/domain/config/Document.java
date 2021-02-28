package site.edkw.crm.docserver.domain.config;

import lombok.Data;

@Data
public class Document {
    private String fileType;
    private String title;
    private String url;
    private String key;
}
