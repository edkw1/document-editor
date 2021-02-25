package site.edkw.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.edkw.crm.domain.File;


public interface FileRepository extends JpaRepository<File, Long> {
}
