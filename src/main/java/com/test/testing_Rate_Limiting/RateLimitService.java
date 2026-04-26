package com.test.testing_Rate_Limiting;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private final LettuceBasedProxyManager<String> proxyManager;

    public RateLimitService(LettuceBasedProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }
    public Bucket resolveBucket(String key){
        BucketConfiguration config=BucketConfiguration.builder().addLimit(limit -> limit.capacity(2).refillGreedy(2, Duration.ofMinutes(1)))
                .build();//creates bucket with configuration og 2 token capacity and 2 token generation per minute
        return proxyManager.builder().build(key, () -> config); //if bucket exist ruse or create a new bucket
    }
}
//each user(different key/userid) gets a single bucket independent of each other