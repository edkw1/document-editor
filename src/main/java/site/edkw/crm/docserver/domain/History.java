package site.edkw.crm.docserver.domain;

import lombok.Data;

@Data
public class History{
    private String serverVersion;
    private Change[] changes;
}
