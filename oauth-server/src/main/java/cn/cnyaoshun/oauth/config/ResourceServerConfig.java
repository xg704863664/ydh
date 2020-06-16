package cn.cnyaoshun.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig implements ResourceServerConfigurer {

    @Override
    public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) throws Exception {
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
                 httpSecurity
                         .authorizeRequests()
                         .mvcMatchers("/user/**").authenticated()
                         .mvcMatchers("/organization/**").authenticated()
                         .mvcMatchers("/project/**").authenticated()
                         .mvcMatchers("/department/**").authenticated()
                         .mvcMatchers("/role/**").authenticated()
                         .mvcMatchers("/account/**").authenticated();
    }
}
