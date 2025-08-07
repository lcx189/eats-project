package com.eats.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("Redisテンプレートオブジェクトの作成を開始しま�?..");
        RedisTemplate redisTemplate = new RedisTemplate();
        //Redisの接続ファクトリオブジェクトを設定します
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //Redisキーのシリアライザを設定しま�?
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
