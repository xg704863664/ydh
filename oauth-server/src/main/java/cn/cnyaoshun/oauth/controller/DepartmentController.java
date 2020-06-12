package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
