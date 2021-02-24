package site.edkw.crm.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Data
public class File extends BaseEntity {
    @Column(name = "name")
    String name;

    @Column(name = "path")
    String path;

    @Column(name = "size")
    long size;
}
