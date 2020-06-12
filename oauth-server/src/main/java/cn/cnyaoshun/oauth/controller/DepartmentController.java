package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV2;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV3;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/department")
@Api(description = "部门操作API")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @ApiOperation(value = "根据机构id获取部门树结构",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/get/{organizationId}",method = RequestMethod.GET)
    public ReturnJsonData< List<DepartmentDomain>> getDepartmentTree(@ApiParam(value = "组织机构id",required = true)@PathVariable(value = "organizationId") Long organizationId){
        List<DepartmentDomain> departmentDomainList = departmentService.findByOrganizationId(organizationId);
        return ReturnJsonData.build(departmentDomainList);
    }

    @ApiOperation(value = "新增部门",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertDepartment(@ApiParam(value = "新增部门",required = true) @Valid @RequestBody DepartmentDomainV2 departmentDomainV2){
        Long departmentId = departmentService.insertDepartment(departmentDomainV2);
        return ReturnJsonData.build(departmentId);
    }

    @ApiOperation(value = "删除部门",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{departmentId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "部门id",required = true)@PathVariable(value = "departmentId") Long departmentId){
        departmentService.deleteDepartment(departmentId);
        return ReturnJsonData.build(departmentId);
    }

    @ApiOperation(value = "修改",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody DepartmentDomainV3 departmentDomainV3){
        Long departmentId = departmentService.updateDepartment(departmentDomainV3);
        return ReturnJsonData.build(departmentId);
    }
}
