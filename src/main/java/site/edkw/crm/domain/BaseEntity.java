package site.edkw.crm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Audited
@MappedSuperclass
@Data
public class BaseEntity {
    @JsonView(Views.Id.class)
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.FullInfo.class)
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "creator_id", updatable = false)
    private User creator;

    @JsonView(Views.FullInfo.class)
    @CreatedDate
    @Column(name = "created")
    private Date created;

    @JsonView(Views.FullInfo.class)
    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "modifier_id")
    private User modifier;

    @JsonView(Views.FullInfo.class)
    @LastModifiedDate
    @Column(name = "modified")
    private Date modified;

    @JsonView(Views.FullInfo.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
