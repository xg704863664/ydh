package cn.cnyaoshun.form.designer.service.impl;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.common.domain.OauthUserListDomain;
import cn.cnyaoshun.form.common.exception.ExceptionDataNotExists;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.repository.DesignerRepository;
import cn.cnyaoshun.form.designer.service.DesignerService;
import cn.cnyaoshun.form.interceptor.LoginInterceptor;
import cn.cnyaoshun.form.remote.OauthServerClient;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DesignerServiceImpl implements DesignerService {

    @Resource
    private DesignerRepository designerRepository;

    @Resource
    private OauthServerClient oauthServerClient;

    @Override
    public Designer findById(Long id) {
        return designerRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
    }

    @Override
    public List<Designer> findByOrgIdAndStatus(Long orgId, boolean status) {
        return designerRepository.findByOrgIdAndStatus(orgId, status);
    }

    @Override
    public PageDataDomain<Designer> findByPage(Integer pageNum, Integer pageSize, Long orgId) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Designer> page = designerRepository.findByOrgId(orgId, pageable);
        PageDataDomain<Designer> result = new PageDataDomain<Designer>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);
        result.setPages(page.getTotalPages());
        result.setTotal(page.getTotalElements());
        result.setRecords(page.getContent());
        return result;
    }

    @Override
    public Designer save(Designer designer) {
        String token = LoginInterceptor.threadLocal.get();
        ReturnJsonData<OauthUserListDomain> userInfo = oauthServerClient.getUserInfo(token);
        if (designer.getId() == null) {
            designer.setCreateUserName(userInfo.getCode() == 0 ? userInfo.getData().getUserName() : "");
        }
        designer.setUpdateUserName(userInfo.getCode() == 0 ? userInfo.getData().getUserName() : "");
        return designerRepository.save(designer);
    }

    @Override
    public void delete(Long id) {
        designerRepository.deleteById(id);
    }

    @Override
    public Designer updateStatus(Long id, boolean status) {
        Designer designer = designerRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
        designer.setStatus(status);
        return designerRepository.save(designer);
    }

    @Override
    public List<String> findFeildNameById(Long id) {
        Designer designer = designerRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
        Map<String, Map<String, Object>> objects = (Map<String, Map<String, Object>>) JSON.parse(designer.getValue());
        List<String> result = new ArrayList<>();
        objects.entrySet().forEach(entry -> {
            result.add(entry.getKey());
        });
        return result;
    }
}
