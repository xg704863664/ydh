package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.AccountRepository;
import cn.cnyaoshun.oauth.dao.OauthUserListDao;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.domain.OauthUserListDomain;
import cn.cnyaoshun.oauth.domain.PermissionOauthUserListDomain;
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

/**
 * @ClassName OauthServiceImpl
 * @Description TOKEN操作service实现类
 * @Author fyh
 * Date 2020/6/1910:11
 */
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final OauthUserListDao oauthUserListDao;

    @Override
    public OauthUserListDomain getAllUserInfo(OAuth2Authentication oAuth2Authentication,Long projectId) {

        OauthUserListDomain oauthUserListDomain = new OauthUserListDomain();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        UserDetailsImpl userAuthenticationPrincipal = (UserDetailsImpl)userAuthentication.getPrincipal();
        String accountName = userAuthenticationPrincipal.getUsername();
        Long accountId = userAuthenticationPrincipal.getId();
        List<RoleFindAllByProjectIdAndAccountDomain> roleList = oauthUserListDao.getAllRoleDomain(accountId, projectId);
        List<Long> roleIdList = new ArrayList<>();
        roleList.forEach(roleDomainV3 -> {
            roleIdList.add(roleDomainV3.getId());
        });
        if(!roleIdList.isEmpty()){
            List<PermissionOauthUserListDomain> permissionList = oauthUserListDao.getAllPermissionList(roleIdList);
            oauthUserListDomain.setPermissionList(permissionList);
        }
        Account account = accountRepository.findByAccountName(accountName);
        oauthUserListDomain.setRoleList(roleList);
        oauthUserListDomain.setAccountId(accountId);
        if(account.getUserId() != null){
            Optional<User> userOptional = userRepository.findById(account.getUserId());
            userOptional.ifPresent(user -> {
                oauthUserListDomain.setUserName(user.getUserName());
                oauthUserListDomain.setUserId(user.getId());
            });
        }
        return oauthUserListDomain;
    }
}
