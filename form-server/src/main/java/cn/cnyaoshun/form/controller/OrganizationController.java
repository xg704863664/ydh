package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.service.DesignerService;
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
import javax.validation.constraints.Min;
import java.awt.*;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/7/8 18:00
 */
@RestController
@RequestMapping("/organization")
@Api(description = "表单组织目录")
@Validated
@Deprecated
public class OrganizationController {
    @Resource
    private OrganizationService organizationService;
    @Resource
    private DesignerService designerService;

    @Deprecated
    @ApiOperation(value = "查询表单全部目录", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findAll")
    public ReturnJsonData findAll() {
        return ReturnJsonData.build(organizationService.getAll());
    }

    @Deprecated
    @ApiOperation(value = "修改新增目录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("save")
    public ReturnJsonData save(@ApiParam(value = "新增修改目录", required = true) @RequestBody @Valid Organization organization) {
        return ReturnJsonData.build(organizationService.save(organization));
    }

    @Deprecated
    @ApiOperation(value = "根据id删除目录", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping("delete/{id}")
    public ReturnJsonData delete(@ApiParam(value = "目录id", required = true) @PathVariable(value = "id") Long id) {
        int count = designerService.countByOrgId(id);
        if (count>0){
            return ReturnJsonData.build(403,"请先情况目录下表单,再执行此操作");
        }
        organizationService.delete(id);
        return ReturnJsonData.build();
    }

    @Deprecated
    @ApiOperation(value = "加载分页列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findByPage")
    public ReturnJsonData<PageDataDomain<Organization>> findByPage(@Min(1) @ApiParam(value = "当前页", required = true) @RequestParam(value = "pageNumber") Integer pageNumber,
                                                               @Min(1) @ApiParam(value = "每页显示数量", required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                                               String name) {
        PageDataDomain<Organization> result = organizationService.findByPage(pageNumber, pageSize, name);
        return ReturnJsonData.build(result);
    }
}
