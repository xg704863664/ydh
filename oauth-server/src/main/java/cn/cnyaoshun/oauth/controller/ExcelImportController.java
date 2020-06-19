package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.common.util.RedisTokenUtil;
import cn.cnyaoshun.oauth.service.ExcelImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/import")
@Api(description = "excel导入操作API")
@RequiredArgsConstructor
@Validated
public class ExcelImportController {

    private final ExcelImportService excelImportService;

    private final RedisTokenUtil redisTokenUtil;

    @RequestMapping(value = "/org/department/download", method = RequestMethod.GET)
    @ApiOperation(value = "下载组织机构模版", httpMethod = "GET")
    public ResponseEntity<byte[]> downloadOrgDepartmentExcel() {
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            File file = ResourceUtils.getFile("classpath:organization_template.xlsx");
            bytes = FileUtils.readFileToByteArray(file);
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("organization_template.xlsx","utf-8"));
            httpHeaders.set("Content-Type", "application/octet-stream");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/excel/token", method = RequestMethod.GET)
    @ApiOperation(value = "获取excel操作token", httpMethod = "GET")
    public ReturnJsonData<String> getOperationExcelToken() {
        String token = redisTokenUtil.createToken();
        return ReturnJsonData.build(token);
    }


    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    @ApiOperation(value = "excel导入", httpMethod = "POST")
    public ReturnJsonData<String> importOrgDepartmentExcel(@NotNull @ApiParam(value = "file 文件") @RequestParam("file")MultipartFile file,
                                           @NotBlank(message = "operationToken 不能为空") @ApiParam(value ="operationToken 操作token 防重复提交",required = true) @RequestParam(value = "operationToken") String operationToken,
                                           @NotBlank(message = "dealType处理类型不能为空") @ApiParam(value ="dealType处理类型 org_department_deal:组织机构批量导入",required = true) @RequestParam(value = "dealType") String dealType) {
        if (!redisTokenUtil.checkToken(operationToken)){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"operationToken 无效token");
        }
        redisTokenUtil.clearToken(operationToken);
        excelImportService.dealExcel(dealType,file);
        return ReturnJsonData.build("success");
    }
}
