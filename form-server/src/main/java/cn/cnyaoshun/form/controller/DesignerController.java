package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.service.DesignerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/designer")
@Api(description = "设计器管理")
@Validated
public class DesignerController {

    @Resource
    private DesignerService designerService;

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @ApiOperation(value = "新增/修改保存", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/save")
    public ReturnJsonData<Designer> save(@RequestBody Designer designer) {
        return ReturnJsonData.build(designerService.save(designer));
    }

    @ApiOperation(value = "加载详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findById/{id}")
    public ReturnJsonData<Designer> findById(@PathVariable(value = "id") Long id) {
        return ReturnJsonData.build(designerService.findById(id));
    }

    @ApiOperation(value = "加载分页列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findByPage")
    public ReturnJsonData<PageDataDomain<Designer>> findByPage(@Min(1) @ApiParam(value = "当前页", required = true) @RequestParam(value = "pageNumber") Integer pageNumber,
                                                               @Min(1) @ApiParam(value = "每页显示数量", required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                                               @ApiParam(value = "组织机构id", required = true) @RequestParam(value = "orgId") Long orgId) {
        return ReturnJsonData.build(designerService.findByPage(pageNumber, pageSize, orgId));
    }

    @ApiOperation(value = "根据ID删除设计器模版", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping(value = "/delete/{id}")
    public ReturnJsonData delete(@ApiParam(value = "ID", required = true) @PathVariable(value = "id") Long id) {
        designerService.delete(id);
        return ReturnJsonData.build();
    }

    @ApiOperation(value = "修改状态", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/updateStatus")
    public ReturnJsonData<Designer> updateStatus(@ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id,
                                                 @ApiParam(value = "状态", required = true) @RequestParam(value = "status") boolean status) {
        return ReturnJsonData.build(designerService.updateStatus(id, status));
    }

    @ApiOperation(value = "根据目录id查询目录下设计器模版",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findDesigners/{orgId}")
    public ReturnJsonData findDesigners(@ApiParam(value = "目录id",required = true)@PathVariable(value = "orgId")Long orgId){
        return ReturnJsonData.build(designerService.findByOrgIdAndStatus(orgId,true));
    }

    @ApiOperation(value = "根据设计器id查询字段名",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findFeildNameById/{id}")
    public ReturnJsonData<List<String>> findFeildNameById(@ApiParam(value = "设计器id",required = true)@PathVariable(value = "id")Long id){
        Designer designer = designerService.findById(id);
        List<String> result = dataSourceConfigService.findFeildNameByIdAndTableName(designer.getDataSourceId(), designer.getTableName());
        return ReturnJsonData.build(result);
    }
}
