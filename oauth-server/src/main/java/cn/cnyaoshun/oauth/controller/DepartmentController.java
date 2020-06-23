package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.DepartmentTreeDomain;
import cn.cnyaoshun.oauth.domain.DepartmentAddDomain;
import cn.cnyaoshun.oauth.domain.DepartmentUpdateDomain;
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
@Api(description = "组织机构管理-部门管理API")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @ApiOperation(value = "根据组织机构ID获取部门信息,并进行树形结构展示",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/get/{organizationId}",method = RequestMethod.GET)
    public ReturnJsonData< List<DepartmentTreeDomain>> getDepartmentTree(@ApiParam(value = "组织机构ID",required = true)@PathVariable(value = "organizationId") Long organizationId){
        List<DepartmentTreeDomain> departmentTreeDomainList = departmentService.findByOrganizationId(organizationId);
        return ReturnJsonData.build(departmentTreeDomainList);
    }

    @ApiOperation(value = "新增部门",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@Valid @RequestBody DepartmentAddDomain departmentAddDomain){
        Long departmentId = departmentService.add(departmentAddDomain);
        return ReturnJsonData.build(departmentId);
    }

    @ApiOperation(value = "根据部门ID删除部门",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{departmentId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "部门ID",required = true)@PathVariable(value = "departmentId") Long departmentId){
        departmentService.delete(departmentId);
        return ReturnJsonData.build(departmentId);
    }

    @ApiOperation(value = "根据部门ID修改部门信息",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody DepartmentUpdateDomain departmentUpdateDomain){
        Long departmentId = departmentService.update(departmentUpdateDomain);
        return ReturnJsonData.build(departmentId);
    }
}
