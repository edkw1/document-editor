package site.edkw.crm.docserver.domain;

import lombok.Data;

@Data
public class CallbackRequest{
    private Action[] actions;
    private Object[] changeshistory;
    private String changesurl;
    private int forcesavetype;
    private History history;
    private String key;
    private int status;
    private String url;
    private String userdata;
    private String[] users;
    private String lastsave;

}