package cn.cnyaoshun.oauth.controller;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.*;
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
import java.util.List;

/**
 * @ClassName UserController
 * @Description 用户操作
 * @Author fyh
 * Date 2020/6/410:48
 */
@RestController
@RequestMapping("/user")
@Api(description = "组织机构-用户管理")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;


    @ApiOperation(value = "根据部门ID获取用户信息",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping("/department/list")
    public ReturnJsonData<PageDataDomain<UserFindAllByDepartmentIdDomain>> departmentList(@Min (1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber") Integer pageNumber,
                                                                                          @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")  Integer pageSize,
                                                                                          @NotNull @ApiParam(value = "部门ID", required = true)@RequestParam(value = "departmentId")  Long departmentId,
                                                                                          @ApiParam(value = "根据用户名或账户搜索用户信息")@RequestParam(value = "keyWord",required = false) String keyWord){

        return ReturnJsonData.build(userService.findAll(departmentId, keyWord, pageNumber, pageSize));
    }

    @ApiOperation(value = "根据部门ID获取部门下的用户数量",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/count/{departmentId}",method = RequestMethod.GET)
    public ReturnJsonData<Long> countUser(@ApiParam(value = "部门ID" , required = true)@PathVariable(value = "departmentId") Long departmentId){
        Long countUser = userService.countByUserId(departmentId);
        return  ReturnJsonData.build(countUser);
    }

    @ApiOperation(value = "新增用户",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> insertUser(@ApiParam(value = "新增用户对象",required = true) @Valid @RequestBody UserAddDomain userAddDomain){
        Long id = userService.add(userAddDomain);
        return ReturnJsonData.build(id);
    }

    @ApiOperation(value = "根据用户ID修改用户信息",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> updateOrganization(@Valid @RequestBody UserUpdateDomain userUpdateDomain){
        Long userId = userService.update(userUpdateDomain);
        return ReturnJsonData.build(userId);
    }

    @ApiOperation(value = "根据用户ID删除用户信息",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{userId}",method = RequestMethod.DELETE)
    public ReturnJsonData<Long> deleteOrganization(@ApiParam(value = "用户ID",required = true)@PathVariable(value = "userId") Long userId){
        Long deleteUser = userService.delete(userId);
        return ReturnJsonData.build(deleteUser);
    }

    @ApiOperation(value = "获取所有用户名称",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value="/findAllUserName",method = RequestMethod.GET)
    public ReturnJsonData<List<UserFindAllDomain>> findAllByUserName(){
        List<UserFindAllDomain> allUserName = userService.findAllUserName();
        return  ReturnJsonData.build(allUserName);
    }


    @ApiOperation(value = "用户调整部门",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/revise",method = RequestMethod.PUT)
    public ReturnJsonData<Boolean> reviseDepartment(@Valid @RequestBody UserUpdateByDepartmentDomain userUpdateByDepartmentDomain){
        boolean b = userService.reviseDepartment(userUpdateByDepartmentDomain);
        return ReturnJsonData.build(b);
    }

}
