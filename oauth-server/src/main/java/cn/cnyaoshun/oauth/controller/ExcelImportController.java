package cn.cnyaoshun.oauth.controller;

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

@RestController
@RequestMapping("/import")
@Api(description = "excel导入操作API")
@RequiredArgsConstructor
@Validated
public class ExcelImportController {

    private final ExcelImportService excelImportService;

    @RequestMapping(value = "/org/department/download", method = RequestMethod.GET)
    @ApiOperation(value = "下载组织机构模版", httpMethod = "GET")
    public ResponseEntity<byte[]> downloadOrgDepartmentExcel() {
        byte[] bytes = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            File file = ResourceUtils.getFile("classpath:organization_template.xls");
            bytes = FileUtils.readFileToByteArray(file);
            httpHeaders.setContentDispositionFormData("attachment", "organization_template.xls");
            httpHeaders.set("Content-Type", "application/octet-stream");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }


    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    @ApiOperation(value = "excel导入", httpMethod = "POST")
    public String importOrgDepartmentExcel(@NotNull @ApiParam(value = "file 文件") @RequestParam("file")MultipartFile file, @NotBlank(message = "dealType处理类型不能为空") @ApiParam(value ="dealType处理类型" ) @RequestParam(value = "dealType") String dealType) {
        excelImportService.dealExcel(dealType,file);
        return "success";
    }
}
