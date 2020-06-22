package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.ProjectRepository;
import cn.cnyaoshun.oauth.domain.ProjectListDomain;
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
    public List<ProjectListDomain> findAll() {
        List<ProjectListDomain> projectListDomainList = new ArrayList<>();
        List<Project> projectList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        projectList.forEach(project -> {
            ProjectListDomain projectListDomain = new ProjectListDomain();
            BeanUtils.copyProperties(project, projectListDomain);
            projectListDomainList.add(projectListDomain);
        });
        return projectListDomainList;
    }
}
