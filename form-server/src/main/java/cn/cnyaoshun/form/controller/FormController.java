package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.service.DesignerService;
import cn.cnyaoshun.form.designer.service.FormService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/form")
@Api(description = "表单管理")
@Validated
public class FormController {

    @Resource
    private DesignerService designerService;

    @Resource
    private FormService formService;

    /**
     * 加载表头
     *
     * @param designerId
     * @return
     */
    @ApiOperation(value = "加载表头", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/loadTableName/{designerId}")
    public ReturnJsonData<List<Map<String, Object>>> loadTableName(@NotNull @ApiParam(value = "设计器id", required = true) @PathVariable(value = "designerId") Long designerId) {
        Designer designer = designerService.findById(designerId);
        Map<String, Map<String, Object>> objects = (Map<String, Map<String, Object>>) JSON.parse(designer.getValue());
        List<Map<String, Object>> result = new ArrayList<>();
        objects.entrySet().forEach(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("Prop", entry.getKey());
            map.put("label", entry.getValue().get("label"));
            result.add(map);
        });
        return ReturnJsonData.build(result);
    }

    /**
     * 加载分页数据
     *
     * @param pageNumber
     * @param pageSize
     * @param designerId
     * @return
     */
    @ApiOperation(value = "加载分页数据", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/findByPage")
    public ReturnJsonData<PageDataDomain<Map<String, Object>>> findByPage(@Min(1) @ApiParam(value = "当前页", required = true) @RequestParam(value = "pageNumber") Integer pageNumber,
                                                                          @Min(1) @ApiParam(value = "每页显示数量", required = true) @RequestParam(value = "pageSize") Integer pageSize,
                                                                          @NotNull @ApiParam(value = "设计器id", required = true) @RequestParam(value = "designerId") Long designerId) {
        return ReturnJsonData.build(formService.findByPage(pageNumber, pageSize, designerId));
    }

    @ApiOperation(value = "根据id查询数据", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/findById/{designerId}/{id}")
    public ReturnJsonData<Map<String, Object>> findById(@NotNull @ApiParam(value = "设计器id", required = true) @PathVariable(value = "designerId") Long designerId,
                                                        @NotBlank @ApiParam(value = "本数据id", required = true) @PathVariable(value = "id") String id) {
        return ReturnJsonData.build(formService.findById(id, designerId));
    }

    @ApiOperation(value = "根据id删除数据", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping("/delete/{designerId}/{id}")
    public ReturnJsonData delete(@ApiParam(value = "设计器id", required = true) @NotNull @PathVariable(value = "designerId") Long designerId,
                                 @ApiParam(value = "本数据id", required = true) @NotBlank @PathVariable(value = "id") String id) {
        formService.delete(id, designerId);
        return ReturnJsonData.build();
    }

    @ApiOperation(value = "保存数据（修改数据时，需要传id）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/save")
    public ReturnJsonData save(@RequestBody Map<String, Object> map) {
        formService.save(map);
        return ReturnJsonData.build();
    }
}
