package cn.cnyaoshun.oauth.controller;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.service.UserService;
import io.swagger.annotations.Api;
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
@Api(description = "用户组织机构操作Api")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService crmUserService;


    @ApiOperation(value = "根据部门id获取用户列表",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping("/department/list")
    public ReturnJsonData<PageDataDomain<UserDomain>> departmentList(@Min (1)@ApiParam(value = "pageNumber 起始页",required = true)@RequestParam(value = "pageNumber") Integer pageNumber,
                                                           @Min (1)@ApiParam(value = "pageSize 每页显示数量", required = true)@RequestParam(value = "pageSize")  Integer pageSize,
                                                           @NotNull @ApiParam(value = "departmentId 部门id", required = true)@RequestParam(value = "departmentId")  Long departmentId,
                                                           @ApiParam(value = "name 根据用户名称搜索")@RequestParam(value = "name",required = false) String name){

        return ReturnJsonData.build(crmUserService.departmentList(pageNumber, pageSize,departmentId,name));
    }


    @ApiOperation(value = "新增用户",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertUser(@ApiParam(value = "新增用户对象",required = true) @Valid @RequestBody UserDomain userDomain){
        Long id = crmUserService.insertUser(userDomain);
        return ReturnJsonData.build(id);
    }
}
