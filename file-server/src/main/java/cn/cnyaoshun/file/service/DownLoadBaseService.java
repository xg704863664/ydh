package cn.cnyaoshun.file.service;

import cn.cnyaoshun.file.entity.FileRecord;
import org.springframework.http.ResponseEntity;

public interface DownLoadBaseService {
    ResponseEntity<byte[]> downLoadFile(FileRecord fileRecord);
}
