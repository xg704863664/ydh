package cn.cnyaoshun.oauth.controller;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;
import cn.cnyaoshun.oauth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by fyh on 2020-6-4.
 * 用户表
 */
@RestController
@RequestMapping("/user")
@Api(description = "用户操作API")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;


    @ApiOperation(value = "根据部门ID获取用户信息",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping("/department/list")
    public ReturnJsonData<PageDataDomain<UserDomain>> departmentList(@Min (1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber") Integer pageNumber,
                                                           @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")  Integer pageSize,
                                                           @NotNull @ApiParam(value = "部门ID", required = true)@RequestParam(value = "departmentId")  Long departmentId,
                                                           @ApiParam(value = "根据用户性别搜索用户信息")@RequestParam(value = "sex",required = false) String sex,
                                                           @ApiParam(value = "根据用户名称搜索用户信息")@RequestParam(value = "name",required = false) String name,
                                                           @ApiParam(value = "根据用户电话搜索用户信息")@RequestParam(value = "phone",required = false) String phone,
                                                           @ApiParam(value = "根据用户工号搜索用户信息")@RequestParam(value = "userNo",required = false) String userNo){

        return ReturnJsonData.build(userService.findAll(departmentId, name, sex, phone, userNo, pageNumber, pageSize));
    }

    @ApiOperation(value = "根据部门ID获取部门下的用户数量",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/count/{departmentId}",method = RequestMethod.GET)
    public ReturnJsonData<Long> countUser(@ApiParam(value = "部门ID" , required = true)@PathVariable(value = "departmentId") Long departmentId){
        Long countUser = userService.countByUserId(departmentId);
        return  ReturnJsonData.build(countUser);
    }

    @ApiOperation(value = "新增用户",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertUser(@ApiParam(value = "新增用户对象",required = true) @Valid @RequestBody UserDomain userDomain){
        Long id = userService.add(userDomain);
        return ReturnJsonData.build(id);
    }

    @ApiOperation(value = "根据用户ID修改用户信息",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody UserDomainV2 userDomainV2){
        Long userId = userService.update(userDomainV2);
        return ReturnJsonData.build(userId);
    }

    @ApiOperation(value = "根据用户ID删除用户信息",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{userId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "用户ID",required = true)@PathVariable(value = "userId") Long userId){
        userService.delete(userId);
        return ReturnJsonData.build(userId);
    }

}
