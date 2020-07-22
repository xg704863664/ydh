package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.AccessTokenUtil;
import cn.cnyaoshun.form.common.ApiCode;
import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.common.domain.OauthUserListDomain;
import cn.cnyaoshun.form.common.exception.ExceptionValidation;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.model.DesignerDomain;
import cn.cnyaoshun.form.designer.service.DesignerService;
import cn.cnyaoshun.form.remote.OauthServerClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/designer")
@Api(description = "设计器管理")
@Validated
public class DesignerController {

    @Resource
    private DesignerService designerService;

    @Autowired
    private OauthServerClient oauthServerClient;

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @ApiOperation(value = "新增/修改保存", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/save")
    public ReturnJsonData<Designer> save(@Valid @RequestBody Designer designer) {
        return ReturnJsonData.build(designerService.save(designer));
    }

    @ApiOperation(value = "加载详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findById/{id}")
    public ReturnJsonData<Designer> findById(@NotNull @PathVariable(value = "id") Long id) {
        return ReturnJsonData.build(designerService.findById(id));
    }

    @ApiOperation(value = "加载分页列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findByPage")
    public ReturnJsonData<PageDataDomain<Designer>> findByPage(@Min(1) @ApiParam(value = "当前页", required = true) @RequestParam(value = "pageNumber") Integer pageNumber,
                                                               @Min(1) @ApiParam(value = "每页显示数量", required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                                               @ApiParam(value = "searchValue根据关键字搜索") @RequestParam(value = "searchValue",required = false)String searchValue) {
        return ReturnJsonData.build(designerService.findByPage(pageNumber, pageSize, searchValue));
    }

    @ApiOperation(value = "根据ID删除设计器模版", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping(value = "/delete/{id}")
    public ReturnJsonData delete(@ApiParam(value = "ID", required = true) @NotNull @PathVariable(value = "id") Long id) {
        designerService.delete(id);
        return ReturnJsonData.build();
    }

    @ApiOperation(value = "修改状态", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/updateStatus")
    public ReturnJsonData<Designer> updateStatus(@NotNull @ApiParam(value = "id", required = true) @NotNull @RequestParam(value = "id") Long id,
                                                 @NotNull @ApiParam(value = "orgId 单位id", required = true) @RequestParam(value = "orgId") Long orgId,
                                                 @ApiParam(value = "状态", required = true) @RequestParam(value = "status") boolean status) {
        return ReturnJsonData.build(designerService.updateStatus(id, status,orgId));
    }


    @ApiOperation(value = "上报系统根据用户信息获取表单列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findDesigners")
    public ReturnJsonData findDesigners() {
        String token = AccessTokenUtil.currentToken();
        ReturnJsonData<OauthUserListDomain> userInfo = oauthServerClient.getUserInfo(token);
        Long orgId = userInfo.getData().getOrgId();
        if (orgId == null){
            throw new ExceptionValidation(ApiCode.DATA_NOT_EXISTS.getCode(),"此用户不在组织机构下");
        }
        return ReturnJsonData.build(designerService.findByOrgIdAndStatus(orgId, true));
    }

    @ApiOperation(value = "根据设计器id查询字段名", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findFeildNameById/{id}")
    public ReturnJsonData<List<String>> findFeildNameById(@ApiParam(value = "设计器id", required = true) @NotNull @PathVariable(value = "id") Long id) {
        Designer designer = designerService.findById(id);
        List<String> result = dataSourceConfigService.findFeildNameByIdAndTableName(designer.getDataSourceId(), designer.getTableName());
        return ReturnJsonData.build(result);
    }

    @ApiOperation(value = "保存模版", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/saveValue")
    public ReturnJsonData<Designer> saveValue(@Valid @RequestBody DesignerDomain designerDomain) {
        Long id = designerDomain.getId();
        String value = designerDomain.getValue();
        Designer designer = designerService.findById(id);
        if (designer.isStatus()) {
            return ReturnJsonData.build(999, "已发布的数据，不可进行修改表单设计器的内容！");
        }
        designer.setValue(value);
        return ReturnJsonData.build(designerService.save(designer));
    }

    @ApiOperation(value = "校验名字是否重复", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/checkName")
    public ReturnJsonData checkName(@NotBlank @ApiParam(value = "name", required = true) @RequestParam(value = "name") String name){
        List<Designer> byName = designerService.findByName(name);
        if(byName.size()>0){
            return ReturnJsonData.build(999,"表单名称重复！");
        }else{
            return ReturnJsonData.build("名字可用！");
        }
    }
}
