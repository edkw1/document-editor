package site.edkw.crm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Data
public class FileInfo extends BaseEntity {
    @JsonView(Views.File.class)
    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @JsonView(Views.File.class)
    @Column(name = "size")
    private long size;

    @Column(name = "type")
    private String type;

    @Column(name = "editor_key")
    private String editorKey;
}
