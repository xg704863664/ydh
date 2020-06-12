package cn.cnyaoshun.file.controller;

import cn.cnyaoshun.file.entity.FileRecord;
import cn.cnyaoshun.file.remote.OauthServerClient;
import cn.cnyaoshun.file.common.ApiCode;
import cn.cnyaoshun.file.common.ReturnJsonData;
import cn.cnyaoshun.file.common.exception.ExceptionAuth;
import cn.cnyaoshun.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/file")
@Api(description = "文件操作")
@RequiredArgsConstructor
@Validated
public class FileController {
    private final OauthServerClient oauthServerClient;
    private final FileService fileService;
    @ApiOperation(value = "文件上传", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data", consumes = "multipart/form-data")
    public ReturnJsonData<Long> uploadFile(@NotBlank(message = "token不能为空")@RequestHeader(value = "Authorization") String token, @ApiParam(value = "file 文件") @RequestParam("file") MultipartFile file) {
        ReturnJsonData<String> returnJsonData = oauthServerClient.checkToken(token);
        if (417 == returnJsonData.getCode()) {
            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(), ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
        }
        FileRecord fileRecord = fileService.uploadFile(file);
        return ReturnJsonData.build(fileRecord.getId());
    }


    @RequestMapping(value = "/download/{fileId}",method = RequestMethod.GET)
    @ApiOperation(value = "下载文件",httpMethod = "GET")
    public ResponseEntity<byte[]> downloadFile(
             @NotBlank(message = "token不能为空") @RequestHeader("Authorization")String token,
             @NotNull(message = "fileId不能为null")@ApiParam(value = "fileId 文件id",required = true) @PathVariable(value = "fileId") Long fileId){

        ReturnJsonData<String> returnJsonData = oauthServerClient.checkToken(token);
        if (417 == returnJsonData.getCode()) {
            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(), ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
        }
        return fileService.downLoadFile(fileId);
    }


    @RequestMapping(value = "/preview/{fileId}",method = RequestMethod.GET,produces = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_JPEG_VALUE})
    @ApiOperation(value = "预览图片",httpMethod = "GET")
    public ResponseEntity<Resource> previewImage(@NotNull(message = "fileId不能为null")@ApiParam(value = "fileId 文件id",required = true) @PathVariable(value = "fileId") Long fileId){
        return fileService.previewImage(fileId);
    }
}
