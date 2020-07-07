package cn.cnyaoshun.from;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringCloudApplication
@EnableJpaAuditing
@EnableFeignClients
public class FormServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormServerApplication.class,args);
    }
}
