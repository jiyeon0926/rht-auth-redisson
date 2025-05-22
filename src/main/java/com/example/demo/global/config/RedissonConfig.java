package com.example.demo.global.config;

import com.example.demo.global.common.constants.RedisConstants;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(RedisConstants.REDIS_URL_PREFIX + host + ":" + port);

        // JSON 형식으로 직렬화 및 역직렬화
        config.setCodec(new org.redisson.codec.JsonJacksonCodec());

        return Redisson.create(config);
    }
}
