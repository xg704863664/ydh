package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.OauthUserListDomain;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 根据项目ID和TOKEN获取用户信息
 */
public interface OauthService {

    OauthUserListDomain getAllUserInfo(OAuth2Authentication oAuth2Authentication, Long projectId);
}
