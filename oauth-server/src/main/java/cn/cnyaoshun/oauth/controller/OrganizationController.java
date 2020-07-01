package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.OrganizationAddDomain;
import cn.cnyaoshun.oauth.domain.OrganizationUpdateDomain;
import cn.cnyaoshun.oauth.domain.OrganizationFindAllDomain;
import cn.cnyaoshun.oauth.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by fyh on 2020-6-11.
 */
@RestController
@RequestMapping("/organization")
@Api(description = "组织机构管理")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private  final  OrganizationService organizationService;

    @ApiOperation(value = "新增组织机构",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertUser(@Valid @RequestBody OrganizationAddDomain OrganizationAddDomain){
        Long id = organizationService.add(OrganizationAddDomain);
        return ReturnJsonData.build(id);
    }

    @ApiOperation(value = "根据组织机构ID修改组织机构信息",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody OrganizationUpdateDomain organizationUpdateDomain){
        Long organizationId = organizationService.update(organizationUpdateDomain);
        return ReturnJsonData.build(organizationId);
    }

    @ApiOperation(value = "获取所有的组织机构信息",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ReturnJsonData<OrganizationFindAllDomain> findAll(){
        List<OrganizationFindAllDomain> organizationList = organizationService.findAll();
        return ReturnJsonData.build(organizationList);
    }

    @ApiOperation(value = "根据组织机构ID删除组织机构",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{organizationId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "组织机构ID",required = true)@PathVariable(value = "organizationId") Long organizationId){
        organizationService.delete(organizationId);
        return ReturnJsonData.build(organizationId);
    }
}
