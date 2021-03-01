package site.edkw.crm.docserver.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Action{
    public static final int TYPE_USER_CONNECTED = 1;
    public static final int TYPE_USER_DISCONNECTED = 0;


    private int type;

    @JsonProperty("userid")
    private String userId;
}
