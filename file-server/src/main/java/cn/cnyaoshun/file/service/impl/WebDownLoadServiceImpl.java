package cn.cnyaoshun.file.service.impl;

import cn.cnyaoshun.file.common.ApiCode;
import cn.cnyaoshun.file.common.DownloadType;
import cn.cnyaoshun.file.common.annotation.HandlerType;
import cn.cnyaoshun.file.common.exception.ExceptionValidation;
import cn.cnyaoshun.file.entity.FileRecord;
import cn.cnyaoshun.file.service.DownLoadBaseService;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@Service
@HandlerType(value = DownloadType.WEB)
public class WebDownLoadServiceImpl implements DownLoadBaseService {
    @Override
    public ResponseEntity<byte[]> downLoadFile(FileRecord fileRecord) {
        byte[] bytes;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        File file = new File(fileRecord.getPath());
        try {
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode(fileRecord.getName(),"utf-8"));
//            httpHeaders.set("Content-Type", "application/octet-stream");
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new ExceptionValidation(ApiCode.DOWNLOAD_File_FAIL.getCode(), ApiCode.DOWNLOAD_File_FAIL.getMsg());
        }
        if (bytes == null){
            throw new ExceptionValidation(ApiCode.FILE_NOT_EXIST.getCode(),ApiCode.FILE_NOT_EXIST.getMsg());
        }
        return new ResponseEntity<>(bytes,httpHeaders, HttpStatus.OK);
    }
}
