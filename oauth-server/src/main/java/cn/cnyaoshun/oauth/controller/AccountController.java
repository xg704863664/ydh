package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.AccountFindAllByRoleIdDomain;
import cn.cnyaoshun.oauth.domain.AccountAddDomain;
import cn.cnyaoshun.oauth.domain.AccountFindAllDomain;
import cn.cnyaoshun.oauth.domain.AccountUpdateDomain;
import cn.cnyaoshun.oauth.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName AccountController
 * @Description 账户操作
 * @Author fyh
 * Date 2020/6/1610:48
 */
@RestController
@RequestMapping("/account")
@AllArgsConstructor
@Api(description = "账户管理API")
@Validated
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "根据角色ID获取账户列表",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll/{roleId}", method = RequestMethod.GET)
    public ReturnJsonData<PageDataDomain<AccountFindAllByRoleIdDomain>> findAllByRoleId(@NotNull @ApiParam(value = "角色ID",required = true) @PathVariable(name = "roleId") Long roleId,
                                                                                        @ApiParam(value = "根据账号搜索账户信息") @RequestParam(value = "keyWord",required = false) String keyWord,
                                                                                        @Min(1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber")Integer pageNumber,
                                                                                        @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")Integer pageSize ){
        PageDataDomain<AccountFindAllByRoleIdDomain> accountList = accountService.findAllByRoleId(roleId, keyWord, pageNumber, pageSize);
        return ReturnJsonData.build(accountList);
    }

    @ApiOperation(value = "新增账户为其赋予角色",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ReturnJsonData<Long> add(@Valid @RequestBody AccountAddDomain accountAddDomain){
        Long accountId = accountService.add(accountAddDomain);
        return ReturnJsonData.build(accountId);
    }

    @ApiOperation(value = "根据账户ID删除账户信息",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/delete/{accountId}")
    public ReturnJsonData<Long> delete(@NotNull @ApiParam(value = "账户ID") @RequestParam(value = "accountId") Long accountId){
        Long deleteAccount = accountService.delete(accountId);
        return ReturnJsonData.build(deleteAccount);
    }

    @ApiOperation(value = "根据账户ID,修改账户信息",httpMethod = "PUT",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ReturnJsonData<Long> update(@Valid @RequestBody AccountUpdateDomain accountUpdateDomain){
        Long id = accountService.update(accountUpdateDomain);
        return ReturnJsonData.build(id);
    }

    @ApiOperation(value = "获取账户所有信息",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public ReturnJsonData<PageDataDomain<AccountFindAllDomain>> findAll(@ApiParam(value = "根据账号搜索账户信息") @RequestParam(value = "keyWord",required = false) String keyWord,
                                                                        @Min(1)@ApiParam(value = "起始页",required = true)@RequestParam(value = "pageNumber")Integer pageNumber,
                                                                        @Min (1)@ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")Integer pageSize ){
        PageDataDomain<AccountFindAllDomain> domainPageDataDomain = accountService.findAll(pageNumber, pageSize, keyWord);
        return ReturnJsonData.build(domainPageDataDomain);
    }

}
