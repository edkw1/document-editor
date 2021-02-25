package site.edkw.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.edkw.crm.domain.File;
import site.edkw.crm.domain.Status;

import java.util.List;


public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByStatus(Status status);
}
