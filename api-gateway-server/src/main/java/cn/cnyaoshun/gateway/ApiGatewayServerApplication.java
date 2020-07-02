package cn.cnyaoshun.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class ApiGatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServerApplication.class,args);
    }
}
