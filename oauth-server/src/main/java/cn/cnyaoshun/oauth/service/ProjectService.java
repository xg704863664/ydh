package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.ProjectListDomain;

import java.util.List;

public interface ProjectService {
    List<ProjectListDomain> findAll();
}
