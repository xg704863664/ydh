package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.ProjectRepository;
import cn.cnyaoshun.oauth.domain.ProjectDomain;
import cn.cnyaoshun.oauth.entity.Project;
import cn.cnyaoshun.oauth.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;


    /**
     * 获取项目列表
     * @return
     */
    @Override
    public List<ProjectDomain> list() {
        List<ProjectDomain> projectDomainList = new ArrayList<>();
        List<Project> projectList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        projectList.forEach(project -> {
            ProjectDomain projectDomain = new ProjectDomain();
            BeanUtils.copyProperties(project,projectDomain);
            projectDomainList.add(projectDomain);
        });
        return projectDomainList;
    }
}
