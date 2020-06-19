package cn.cnyaoshun.oauth.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 防表单重复提交token
 */
@Component
@RequiredArgsConstructor
public class RedisTokenUtil {

    private final RedisTemplate<String,String> redisTemplate;

    public String createToken(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        redisTemplate.opsForValue().set(uuid,uuid,60, TimeUnit.SECONDS);
        return uuid;
    }

    public boolean checkToken(String token){
        return redisTemplate.hasKey(token);
    }
}
