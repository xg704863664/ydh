package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.ProjectRoleTreeDomain;
import cn.cnyaoshun.oauth.domain.RoleAddDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByProjectIdAndAccountDomain;
import cn.cnyaoshun.oauth.domain.RoleUpdateDomain;
import cn.cnyaoshun.oauth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @ClassName RoleController
 * @Description 角色操作
 * @Author fyh
 * Date 2020-6-1514:52
 */
@RestController
@RequestMapping("/role")
@Api(description = "角色管理API")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @ApiOperation(value = "新增角色并分配权限",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@ApiParam(value = "新增角色对象",required = true) @Valid @RequestBody RoleAddDomain roleAddDomain){
        Long roleAdd = roleService.add(roleAddDomain);
        return ReturnJsonData.build(roleAdd);
    }

    @ApiOperation(value = "根据角色ID修改角色", httpMethod = "PUT" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ReturnJsonData<Long> update(@ApiParam(value = "修改角色对象",required = true) @Valid @RequestBody RoleUpdateDomain roleUpdateDomain){
        Long updateId = roleService.update(roleUpdateDomain);
        return  ReturnJsonData.build(updateId);
    }

    @ApiOperation(value = "根据角色ID删除角色信息",httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    public ReturnJsonData<Long> delete(@ApiParam(value = "角色ID",required = true) @PathVariable(name = "roleId") Long roleId){
        Long deleteRole = roleService.delete(roleId);
        return ReturnJsonData.build(deleteRole);
    }

    @ApiOperation(value = "根据项目ID获取角色信息",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll/{projectId}", method = RequestMethod.GET)
    public ReturnJsonData<List<RoleUpdateDomain>> findAllByProjectId(@ApiParam(value = "项目ID",required = true) @PathVariable(name = "projectId") Long projectId){
        List<RoleUpdateDomain> roleDomainList = roleService.findAllByProjectId(projectId);
        return  ReturnJsonData.build(roleDomainList);
    }

    @ApiOperation(value = "获取所有的角色名称和ID",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAllRoleNameAndId", method = RequestMethod.GET)
    public ReturnJsonData<PageDataDomain<RoleFindAllByProjectIdAndAccountDomain>> findAllRoleNameAndId(@Min(1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber")Integer pageNumber,
                                                                                                       @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")Integer pageSize,
                                                                                                       @ApiParam(value = "条件筛选角色名称")@RequestParam(value = "RoleName",required = false) String roleName){
        PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> all = roleService.findAll(pageNumber,pageSize,roleName);
        return ReturnJsonData.build(all);
    }

    @ApiOperation(value = "获取项目角色树",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findRoleTree",method = RequestMethod.GET)
    public ReturnJsonData<List<ProjectRoleTreeDomain>> findRoleTree(){
        List<ProjectRoleTreeDomain> roleTree = roleService.findAllRoleTree();
        return ReturnJsonData.build(roleTree);
    }


}
