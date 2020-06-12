package cn.cnyaoshun.file.service;

import cn.cnyaoshun.file.entity.FileRecord;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileRecord uploadFile(MultipartFile file);
    ResponseEntity<byte[]> downLoadFile(Long fileId);
    ResponseEntity<Resource> previewImage(Long fileId);
}
