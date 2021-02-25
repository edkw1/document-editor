package site.edkw.crm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

    @JsonView(Views.ShortInfo.class)
    @Column(name = "username")
    private String username;

    @JsonView(Views.ShortInfo.class)
    @Column(name = "password")
    private String password;

    @JsonView(Views.ShortInfo.class)
    @Column(name = "first_name")
    private String firstName;

    @JsonView(Views.ShortInfo.class)
    @Column(name = "last_name")
    private String lastName;

    @JsonView(Views.ShortInfo.class)
    @Column(name = "email")
    private String email;

    @JsonView(Views.ShortInfo.class)
    @Column(name = "phone")
    private String phone;

    @JsonView(Views.ShortInfo.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

}