package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.OauthUserListDomain;
import cn.cnyaoshun.oauth.entity.*;
import cn.cnyaoshun.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName OauthServiceImpl
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/1910:11
 */
@Service
@RequiredArgsConstructor
public class OauthServiceImpl implements OauthService {

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    @Override
    public OauthUserListDomain getAllUserInfo(OAuth2Authentication oAuth2Authentication,Long projectId) {

        OauthUserListDomain oauthUserListDomain = new OauthUserListDomain();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        UserDetailsImpl userAuthenticationPrincipal = (UserDetailsImpl)userAuthentication.getPrincipal();
        String accountName = userAuthenticationPrincipal.getUsername();
        Long accountId = userAuthenticationPrincipal.getId();
        
        Account account = accountRepository.findByAccountName(accountName);
        List<Permission> permissionList = permissionRepository.findByProjectId(projectId);
        List<Long> permissionIDList = new ArrayList<>();
        List<Long> roleIDList = new ArrayList<>();
        permissionList.forEach(permission -> {
            permissionIDList.add(permission.getId());
        });
        List<Role> roleList = roleRepository.findByProjectId(projectId);
        roleList.forEach(role -> {
            roleIDList.add(role.getId());
        });

        oauthUserListDomain.setPermissionIdList(permissionIDList);
        oauthUserListDomain.setRoleIdList(roleIDList);
        oauthUserListDomain.setAccountId(accountId);

        Optional<User> userOptional = userRepository.findById(account.getId());
        userOptional.ifPresent(user -> {
            oauthUserListDomain.setUserName(user.getUserName());
            oauthUserListDomain.setUserId(user.getId());
        });
        return oauthUserListDomain;
    }
}
