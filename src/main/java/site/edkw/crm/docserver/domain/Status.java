package site.edkw.crm.docserver.domain;

public abstract class Status {
    public static final int BEING_EDITED = 1;
    public static final int READY_FOR_SAVING = 2;
    public static final int SAVING_ERROR = 3;
    public static final int CLOSED_WITH_NO_CHANGES = 4;
    public static final int FORCE_SAVING = 6;
    public static final int ERROR_FORCE_SAVING = 7;
}
