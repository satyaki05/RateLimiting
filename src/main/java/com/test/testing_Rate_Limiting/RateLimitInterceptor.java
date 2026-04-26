package com.test.testing_Rate_Limiting;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component

public class RateLimitInterceptor implements HandlerInterceptor{
    private final RateLimitService rateLimitService;
    public RateLimitInterceptor(RateLimitService rateLimitSerrvice){
        this.rateLimitService=rateLimitSerrvice;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {  //entry point for http requests
        String apiKey=request.getHeader("X-forwarded-for"); //check header is proxy is used
        String key=(apiKey!=null)?apiKey:request.getRemoteAddr(); //if proxy is not present then direct IP is used
        Bucket bucket =rateLimitService.resolveBucket(key); //gets user's bucket
        System.out.println("Available tokens for key "+ key +"token: "+ bucket.getAvailableTokens());
        if(bucket.tryConsume(1)){ //if token available then 1 token is consumed per request
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(bucket.getAvailableTokens())); //Tell client how many requests lef
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());//no tokens request blocked
            response.getWriter().write("Too many requests. Please try again later.");
            return false;
        }
    }
}
