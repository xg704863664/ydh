package cn.cnyaoshun.oauth.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    private static final String apiPath = "/api/**";
    private static final String securityName = "oauth2.0";
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${login.clientId}")
    private String clientId;
    @Value("${login.secret}")
    private String secret;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//                .paths(PathSelectors.ant(apiPath))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));

    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .scopeSeparator("read_write")
                .appName("web-app")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

//    @Bean
//    public UiConfiguration uiConfig() {
//        return UiConfigurationBuilder.builder()
//                .deepLinking(true)
//                .displayOperationId(false)
//                .defaultModelsExpandDepth(1)
//                .defaultModelExpandDepth(1)
//                .defaultModelRendering(ModelRendering.EXAMPLE)
//                .displayRequestDuration(false)
//                .docExpansion(DocExpansion.NONE)
//                .filter(false)
//                .maxDisplayedTags(null)
//                .operationsSorter(OperationsSorter.ALPHA)
//                .showExtensions(false)
//                .tagsSorter(TagsSorter.ALPHA)
//                .validatorUrl(null)
//                .build();
//    }

    private ApiInfo apiInfo(){
        return new ApiInfo("oauth 服务","oauth api文档","v0.0.1",null,new Contact("Xia Gen","","704863664@qq.com"),"Apache License Version 2.0","https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read_write", "read and write operations")
                 };
        return scopes;
    }

    private SecurityScheme securityScheme() {
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(contextPath+"/oauth/token");
//        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant("/oauth-server/oauth/token");
//        OAuth oAuth = new OAuth("oauth2.0",Arrays.asList(scopes()),Arrays.asList(passwordCredentialsGrant));
        SecurityScheme oauth = new OAuthBuilder().name(securityName)
                .grantTypes(Arrays.asList(passwordCredentialsGrant))
                .scopes(Arrays.asList(scopes()))
                .build();

        return oauth;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(securityName, scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }
}
