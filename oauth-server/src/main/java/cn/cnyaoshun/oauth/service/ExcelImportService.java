package cn.cnyaoshun.oauth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {
    void dealExcel(String dealType, MultipartFile multipartFile);
    ResponseEntity<byte[]> downLoadTemplate(String templateType);
}
