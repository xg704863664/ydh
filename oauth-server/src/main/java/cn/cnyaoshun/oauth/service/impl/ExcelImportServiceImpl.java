package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.service.DealExcelService;
import cn.cnyaoshun.oauth.service.ExcelImportService;
import cn.cnyaoshun.oauth.service.handler.HandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * excel导入处理公共入口
 */
@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {
    private final HandlerContext handlerContext;
    @Override
    public void dealExcel(String dealType, MultipartFile multipartFile) {
        DealExcelService dealExcelService = handlerContext.getInstance(dealType);
        dealExcelService.dealExcel(multipartFile);
    }
}
