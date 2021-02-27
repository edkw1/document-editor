package site.edkw.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.edkw.crm.domain.FileInfo;
import site.edkw.crm.domain.Status;

import java.util.List;


public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    List<FileInfo> findAllByStatus(Status status);
}
