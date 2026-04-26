package com.test.testing_Rate_Limiting;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
// Correct package for JDK 17 modules
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    @Bean
    public RedisClient redisClient(){
        return RedisClient.create("redis://localhost:6379");//connects spring with redis
    }
    @Bean(destroyMethod="close")
    public StatefulRedisConnection<String, byte[]> bucket4jConnection(RedisClient redisClient){  //opens a connection to redis
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8,ByteArrayCodec.INSTANCE));
    }
    @Bean
    public LettuceBasedProxyManager<String>proxyManager(StatefulRedisConnection<String,byte[]> connection){   //manages buckets inside redis
        return LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();
    }
}
