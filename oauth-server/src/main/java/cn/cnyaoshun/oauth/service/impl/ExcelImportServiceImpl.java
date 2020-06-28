package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.DownLoadExcelTemplateType;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.service.DealExcelService;
import cn.cnyaoshun.oauth.service.ExcelImportService;
import cn.cnyaoshun.oauth.service.handler.HandlerContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Excel数据处理公共入口
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

    @Override
    public ResponseEntity<byte[]> downLoadTemplate(String templateType) {
        String name = DownLoadExcelTemplateType.getDownLoadTypeName(templateType);
        if (StringUtils.isEmpty(name)){
            throw new ExceptionValidation(ApiCode.DATA_NOT_EXISTS.getCode(),ApiCode.DATA_NOT_EXISTS.getMsg());
        }
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
            bytes = IOUtils.toByteArray(inputStream);
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode(name,"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
}
