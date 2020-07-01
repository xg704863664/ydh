package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.PermissionAddDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllByProjectIdDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllDomain;
import cn.cnyaoshun.oauth.domain.PermissionUpdateDomain;
import cn.cnyaoshun.oauth.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName PermissionController
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/1710:21
 */
@RestController
@RequestMapping(value = "/permission")
@Api(description = "权限管理API")
@AllArgsConstructor
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @ApiOperation(value = "根据项目ID新增权限", httpMethod = "POST",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@Validated @RequestBody PermissionAddDomain permissionAddDomain){
        Long permissionId = permissionService.add(permissionAddDomain);
        return ReturnJsonData.build(permissionId);
    }

    @ApiOperation(value = "根据权限ID删除权限", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{permissionId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> delete(@ApiParam(value = "权限ID",required = true)@PathVariable(value = "permissionId")Long permissionId){
        Long deleteById = permissionService.delete(permissionId);
        return ReturnJsonData.build(deleteById);
    }

    @ApiOperation(value = "根据项目ID获取权限列表",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll/{projectId}" ,method = RequestMethod.GET)
    public ReturnJsonData<List<PermissionFindAllByProjectIdDomain>> findAllByProjectId(@NotNull @ApiParam(value = "项目ID",required = true) @PathVariable(name = "projectId") Long projectId){
        List<PermissionFindAllByProjectIdDomain> allByProjectId = permissionService.findAllByProjectId(projectId);
        return ReturnJsonData.build(allByProjectId);
    }

    @ApiOperation(value = "根据权限ID修改权限信息",httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> update(@Validated @RequestBody PermissionUpdateDomain permissionUpdateDomain){
        Long update = permissionService.update(permissionUpdateDomain);
        return ReturnJsonData.build(update);
    }

    @ApiOperation(value = "获取所有权限信息",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ReturnJsonData<PageDataDomain<PermissionFindAllDomain>> findAll(@Min(1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber")Integer pageNumber,
                                                                           @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")Integer pageSize,
                                                                           @ApiParam(value = "条件筛选权限名称和权限编码")@RequestParam(value = "RoleName",required = false) String keyWord){
        PageDataDomain<PermissionFindAllDomain> domains = permissionService.findAll(pageNumber,pageSize,keyWord);
        return ReturnJsonData.build(domains);
    }
}
