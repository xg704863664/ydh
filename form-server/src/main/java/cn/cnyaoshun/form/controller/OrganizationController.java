package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.organization.model.Organization;
import cn.cnyaoshun.form.organization.service.OrganizationService;
import feign.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.awt.*;

/**
 * @Author:
 * @Date: 2020/7/8 18:00
 */
@RestController
@RequestMapping("/organization")
@Api(description = "表单组织目录")
@Validated
public class OrganizationController {
    @Resource
    private OrganizationService organizationService;

    @ApiOperation(value = "查询表单全部目录", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findAll")
    public ReturnJsonData findAll() {
        return ReturnJsonData.build(organizationService.getAll());
    }

    @ApiOperation(value = "修改新增目录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("save")
    public ReturnJsonData save(@ApiParam(value = "新增修改目录", required = true) @RequestBody @Valid Organization organization) {
        return ReturnJsonData.build(organizationService.save(organization));
    }

    @ApiOperation(value = "根据id删除目录", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping("delete/{id}")
    public ReturnJsonData delete(@ApiParam(value = "目录id", required = true) @PathVariable(value = "id") Long id) {
        organizationService.delete(id);
        return ReturnJsonData.build();
    }
}
