package cn.cnyaoshun.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringCloudApplication
@EnableZuulProxy
public class ApiGatewayServerApplication {

    public static void main(String[] args) {
//        Properties p = new Properties();
//        p.setProperty("server.port","11111");
//        new SpringApplicationBuilder(ApiGatewayServerApplication.class).properties(p).build(args);
        SpringApplication.run(ApiGatewayServerApplication.class,args);
    }
}
