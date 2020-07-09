package cn.cnyaoshun.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringCloudApplication
@EnableHystrix
public class ApiGatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServerApplication.class,args);
    }
}
