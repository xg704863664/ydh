package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.RoleAddDomain;
import cn.cnyaoshun.oauth.domain.RoleUpdateDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByProjectIdAndAccountDomain;
import cn.cnyaoshun.oauth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName RoleController
 * @Description DOTO
 * @Author fyh
 * Date 2020-6-1514:52
 */
@RestController
@RequestMapping("/role")
@Api(description = "角色操作API")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @ApiOperation(value = "新增角色并为其赋予权限",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@ApiParam(value = "新增角色对象",required = true) @Valid @RequestBody RoleAddDomain roleAddDomain){
        Long roleAdd = roleService.add(roleAddDomain);
        return ReturnJsonData.build(roleAdd);
    }

    @ApiOperation(value = "修改角色", httpMethod = "PUT" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ReturnJsonData<Long> update(@ApiParam(value = "修改角色对象",required = true) @Valid @RequestBody RoleUpdateDomain roleUpdateDomain){
        Long updateId = roleService.update(roleUpdateDomain);
        return  ReturnJsonData.build(updateId);
    }

    @ApiOperation(value = "根据角色ID,删除角色信息",httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    public ReturnJsonData<Long> delete(@ApiParam(value = "角色ID",required = true) @PathVariable(name = "roleId") Long roleId){
        Long deleteRole = roleService.delete(roleId);
        return ReturnJsonData.build(deleteRole);
    }

    @ApiOperation(value = "根据项目ID,获取角色信息",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll/{projectId}", method = RequestMethod.GET)
    public ReturnJsonData<List<RoleUpdateDomain>> findAllByProjectId(@ApiParam(value = "项目ID",required = true) @PathVariable(name = "projectId") Long projectId){
        List<RoleUpdateDomain> roleDomainList = roleService.findAllByProjectId(projectId);
        return  ReturnJsonData.build(roleDomainList);
    }

    @ApiOperation(value = "获取所有的角色名称和ID",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAllRoleNameAndId", method = RequestMethod.GET)
    public ReturnJsonData<List<RoleFindAllByProjectIdAndAccountDomain>> findAllRoleNameAndId(){
        List<RoleFindAllByProjectIdAndAccountDomain> all = roleService.findAll();
        return ReturnJsonData.build(all);
    }

}
