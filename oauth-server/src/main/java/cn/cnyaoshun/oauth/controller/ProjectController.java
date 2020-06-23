package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.domain.ProjectListDomain;
import cn.cnyaoshun.oauth.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project")
@Api(description = "项目管理API")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(value = "获取项目列表",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping("/list")
    public ReturnJsonData<List<ProjectListDomain>> list(){
        List<ProjectListDomain> projectListDomainList = projectService.findAll();
        return ReturnJsonData.build(projectListDomainList);
    }

}
