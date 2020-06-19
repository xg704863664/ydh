package cn.cnyaoshun.oauth.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {
    void dealExcel(String dealType, MultipartFile multipartFile);
}
