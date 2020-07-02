package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.AccountRepository;
import cn.cnyaoshun.oauth.dao.OauthUserListDao;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.domain.OauthUserListDomain;
import cn.cnyaoshun.oauth.domain.PermissionOauthUserListDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByOauthDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByProjectIdAndAccountDomain;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDetailsImpl;
import cn.cnyaoshun.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName OauthServiceImpl
 * @Description 获取Tonken信息实现类
 * @Author fyh
 * Date 2020/6/1910:11
 */
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final OauthUserListDao oauthUserListDao;

    /**
     * 根据Token和项目ID获取账户信息及权限
     * @param oAuth2Authentication
     * @param projectId
     * @return
     */
    @Override
    public OauthUserListDomain getAllUserInfo(OAuth2Authentication oAuth2Authentication,Long projectId) {

        OauthUserListDomain oauthUserListDomain = new OauthUserListDomain();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        UserDetailsImpl userAuthenticationPrincipal = (UserDetailsImpl)userAuthentication.getPrincipal();
        String accountName = userAuthenticationPrincipal.getUsername();
        Long accountId = userAuthenticationPrincipal.getId();
        List<RoleFindAllByOauthDomain> roleList = oauthUserListDao.getAllRoleDomain(accountId, projectId);
        List<Long> roleIdList = roleList.stream().map(RoleFindAllByOauthDomain::getId).collect(Collectors.toList());
        Optional.ofNullable(roleIdList).ifPresent(roleIds ->{
            List<PermissionOauthUserListDomain> permissionList = oauthUserListDao.getAllPermissionList(roleIds);
            oauthUserListDomain.setPermissionList(permissionList);
        });
        Account account = accountRepository.findByAccountName(accountName);
        oauthUserListDomain.setRoleList(roleList);
        oauthUserListDomain.setAccountId(accountId);
        Optional.ofNullable(account.getUserId()).ifPresent(userId -> {
            Optional<User> userOptional = userRepository.findById(userId);
            userOptional.ifPresent(user -> {
                oauthUserListDomain.setUserName(user.getUserName());
                oauthUserListDomain.setUserId(user.getId());
            });
        });
        return oauthUserListDomain;
    }
}
