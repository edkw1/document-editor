package site.edkw.crm.domain;

public final class Views {
    public interface Id {};
    public interface ShortInfo extends Id{};
    public interface FullInfo extends ShortInfo {};
    public interface File extends Id{};
}
