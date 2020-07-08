package cn.cnyaoshun.form.datasource.controller;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/dataSource")
@Api(description = "数据源管理")
@AllArgsConstructor
@Validated
public class DataSourceConfigController {

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @ApiOperation(value = "加载数据源分页列表",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findByPage")
    public ReturnJsonData<PageDataDomain<DataSourceConfig>> findByPage(@Min(1) @ApiParam(value = "当前页",required = true)@RequestParam(value = "pageNumber") Integer pageNumber,
                                                                       @Min(1) @ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")  Integer pageSize){
        return ReturnJsonData.build(dataSourceConfigService.findByPage(pageNumber, pageSize));
    }

    @ApiOperation(value = "修改新增数据源",httpMethod = "POST" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/save")
    public ReturnJsonData<DataSourceConfig> save(@ApiParam(value = "修改新增数据源",required = true)@RequestBody @Valid DataSourceConfig dataSourceConfig){
        return ReturnJsonData.build(dataSourceConfigService.save(dataSourceConfig));
    }

    @ApiOperation(value = "根据ID删除数据源",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping(value = "/delete/{id}")
    public ReturnJsonData delete(@ApiParam(value = "数据源ID",required = true)@PathVariable(value = "id") Long id){
        dataSourceConfigService.delete(id);
        return ReturnJsonData.build();
    }

    @ApiOperation(value = "根据ID查询数据源信息",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/findById/{id}")
    public ReturnJsonData findById(@ApiParam(value = "数据源ID",required = true)@PathVariable(value = "id") Long id){
        return ReturnJsonData.build(dataSourceConfigService.findById(id));
    }

    @ApiOperation(value = "测试数据源是否连接成功",httpMethod = "POST",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/testConnect")
    public ReturnJsonData<Boolean> testConnect(@RequestBody DataSourceConfig dataSourceConfig){
        return ReturnJsonData.build(dataSourceConfigService.connect(dataSourceConfig));
    }

    /**
     * 以下为测试方法
     * 联调之后删除
     */
    @ApiOperation(value = "根据ID测试数据源是否连接成功",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/testConnect1/{id}")
    public ReturnJsonData<Boolean> testConnect(@PathVariable(value = "id") Long id){
        return ReturnJsonData.build(dataSourceConfigService.connectById(id));
    }

    @ApiOperation(value = "根据数据源id查询表名",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/getTableName/{id}")
    public ReturnJsonData<List<String>> getTableName(@PathVariable(value = "id") Long id){
        return ReturnJsonData.build(dataSourceConfigService.findTableNameById(id));
    }

    @ApiOperation(value = "根据数据源id和表名查询字段名",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/getTableName/{id}/{tablename}")
    public ReturnJsonData<List<String>> findFeildNameByIdAndTableName(@PathVariable(value = "id") Long id,@PathVariable(value = "tablename") String tablename){
        return ReturnJsonData.build(dataSourceConfigService.findFeildNameByIdAndTableName(id,tablename));
    }

    /**
     * 测试方法结束
     */
}
