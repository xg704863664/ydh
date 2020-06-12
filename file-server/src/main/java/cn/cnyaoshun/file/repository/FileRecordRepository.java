package cn.cnyaoshun.file.repository;

import cn.cnyaoshun.file.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository extends JpaRepository<FileRecord,Long> {
}
