package cn.cnyaoshun.from;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringCloudApplication
@EnableJpaAuditing
@EnableFeignClients
public class FromServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FromServerApplication.class,args);
    }
}
