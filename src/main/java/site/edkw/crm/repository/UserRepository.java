package site.edkw.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.edkw.crm.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
