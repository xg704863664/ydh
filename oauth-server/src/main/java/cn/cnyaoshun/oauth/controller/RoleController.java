package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.RoleDomain;
import cn.cnyaoshun.oauth.domain.RoleDomainV2;
import cn.cnyaoshun.oauth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName RoleController
 * @Description DOTO
 * @Author fyh
 * Date 2020-6-1514:52
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色操作API")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @ApiOperation(value = "新增角色",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@ApiParam(value = "新增角色对象",required = true) @Valid @RequestBody RoleDomain roleDomain){
        Long roleAdd = roleService.add(roleDomain);
        return ReturnJsonData.build(roleAdd);
    }

    @ApiOperation(value = "修改角色", httpMethod = "PUT" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ReturnJsonData<Long> update(@ApiParam(value = "修改角色对象",required = true) @Valid @RequestBody RoleDomainV2 roleDomainV2){
        Long updateId = roleService.update(roleDomainV2);
        return  ReturnJsonData.build(updateId);
    }

    @ApiOperation(value = "根据角色ID,删除角色信息",httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{roleId}", method = RequestMethod.DELETE)
    public ReturnJsonData<Long> delete(@ApiParam(value = "角色ID",required = true) @PathVariable(name = "roleId") Long roleId){
        Long deleteRole = roleService.delete(roleId);
        return ReturnJsonData.build(deleteRole);
    }

}
