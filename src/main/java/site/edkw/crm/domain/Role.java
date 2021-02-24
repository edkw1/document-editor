package site.edkw.crm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@ToString(onlyExplicitlyIncluded = true)
@Data
public class Role{
    @JsonView(Views.Id.class)
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(Views.shortInfo.class)
    @ToString.Include
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
}
