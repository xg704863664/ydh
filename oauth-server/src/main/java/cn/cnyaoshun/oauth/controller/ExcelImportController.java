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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/import")
@Api(description = "Excel操作API")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ExcelImportController {



    // TODO  下载合为一个
    private final ExcelImportService excelImportService;
    private final RedisTokenUtil redisTokenUtil;

    @RequestMapping(value = "/org/department/download", method = RequestMethod.GET)
    @ApiOperation(value = "下载组织机构信息模版", httpMethod = "GET")
    public ResponseEntity<byte[]> downloadOrgDepartmentExcel() {
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("organization_template.xlsx");
            bytes = IOUtils.toByteArray(inputStream);
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("organization_template.xlsx","utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/account/download", method = RequestMethod.GET)
    @ApiOperation(value = "下载账户信息模版", httpMethod = "GET")
    public ResponseEntity<byte[]> downloadAccountExcel() {
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("account_template.xlsx");
            bytes = IOUtils.toByteArray(inputStream);
            httpHeaders.setContentDispositionFormData("attachment", URLEncoder.encode("account_template.xlsx","utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/excel/token", method = RequestMethod.GET)
    @ApiOperation(value = "获取操作Excel的Token", httpMethod = "GET")
    public ReturnJsonData<String> getOperationExcelToken() {
        String token = redisTokenUtil.createToken();
        return ReturnJsonData.build(token);
    }


    @RequestMapping(value = "/excel/import", method = RequestMethod.POST)
    @ApiOperation(value = "Excel数据导入", httpMethod = "POST")
    public ReturnJsonData<String> importExcel(@NotNull @ApiParam(value = "待导入的文件") @RequestParam("file")MultipartFile file,
                                           @NotBlank(message = "Token不能为空") @ApiParam(value ="操作Token防重复提交",required = true) @RequestParam(value = "operationToken") String operationToken,
                                           @NotBlank(message = "类型不能为空") @ApiParam(value ="类型:org_department_deal,表示组织机构信息;account_deal,表示账户信息",required = true) @RequestParam(value = "dealType") String dealType) {
        if (!redisTokenUtil.checkToken(operationToken)){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"获取操作Excel的Token无效");
        }
        redisTokenUtil.clearToken(operationToken);
        excelImportService.dealExcel(dealType,file);
        return ReturnJsonData.build("success");
    }
}
