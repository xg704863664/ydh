package cn.cnyaoshun.form.controller;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.service.DesignerService;
import cn.cnyaoshun.form.designer.service.FormService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FormController {

    @Resource
    private DesignerService designerService;

    @Resource
    private FormService formService;

    /**
     * 加载表头
     * @param designerId
     * @return
     */
    @GetMapping("/loadTableName/{designerId}")
    public ReturnJsonData<List<Map<String,Object>>> loadTableName(@ApiParam(value = "设计器id",required = true)@PathVariable(value = "designerId") Long designerId){
        Designer designer = designerService.findById(designerId);
        Map<String,Map<String,Object>> objects = (Map<String,Map<String,Object>>) JSON.parse(designer.getValue());
        List<Map<String,Object>> result = new ArrayList<>();
        objects.entrySet().forEach(entry -> {
            Map<String,Object> map = new HashMap<>();
            map.put("Prop",entry.getKey());
            map.put("label",entry.getValue().get("label"));
            result.add(map);
        });
        return ReturnJsonData.build(result);
    }

    /**
     * 加载分页数据
     * @param pageNumber
     * @param pageSize
     * @param designerId
     * @return
     */
    public ReturnJsonData<PageDataDomain<Map<String,Object>>> findByPage(@Min(1) @ApiParam(value = "当前页",required = true)@RequestParam(value = "pageNumber") Integer pageNumber,
                                                                               @Min(1) @ApiParam(value = "每页显示数量", required = true)@RequestParam(value = "pageSize")  Integer pageSize,
                                                                               @ApiParam(value = "设计器id", required = true)@RequestParam(value = "designerId")  Long designerId){
        return ReturnJsonData.build(formService.findByPage(pageNumber,pageSize,designerId));
    }

    public ReturnJsonData<Map<String,Object>> findById(@ApiParam(value = "设计器id", required = true)@RequestParam(value = "designerId")  Long designerId,
                                                       @ApiParam(value = "本数据id", required = true)@RequestParam(value = "id")  String id){
        return null;
    }
}
