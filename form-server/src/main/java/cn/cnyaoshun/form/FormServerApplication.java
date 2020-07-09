package cn.cnyaoshun.form;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringCloudApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableHystrix
public class FormServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FormServerApplication.class,args);
    }
}
