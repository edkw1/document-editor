package site.edkw.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.edkw.crm.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
