package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.OrganizationDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Created by fyh on 2020-6-11.
 */
@RestController
@RequestMapping("/organization")
@Api(description = "公司组织机构操作Api")
@RequiredArgsConstructor
@Validated
public class OrganizationController {

    private  final  OrganizationService organizationService;

    @ApiOperation(value = "新增公司组织机构",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertUser(@Valid @RequestBody OrganizationDomain OrganizationDomain){
        Long id = organizationService.insertOrganization(OrganizationDomain);
        return ReturnJsonData.build(id);
    }

    @ApiOperation(value = "修改",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody OrganizationDomainV2 organizationDomainV2){
        Long organizationId = organizationService.updateOrganization(organizationDomainV2);
        return ReturnJsonData.build(organizationId);
    }

    @ApiOperation(value = "查询",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ReturnJsonData<OrganizationDomainV2> organizationList(@Min(1)@ApiParam(value = "pageNumber 起始页", required = true) @RequestParam(value = "pageNumber") Integer pageNumber,
                                                                 @Min(1)@ApiParam(value = "pageSize 每页显示数量",required = true) @RequestParam(value = "pageSize") Integer pageSize
                                                                ){
        PageDataDomain<OrganizationDomainV2> organizationList = organizationService.organizationList(pageNumber, pageSize);
        return ReturnJsonData.build(organizationList);
    }

    @ApiOperation(value = "删除组织机构",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{organizationId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "组织机构id",required = true)@PathVariable(value = "organizationId") Long organizationId){
        organizationService.deleteOrganization(organizationId);
        return ReturnJsonData.build(organizationId);
    }
}
