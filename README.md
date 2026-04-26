Rate Limiter

Overview

A simple rate limiter built with:

Spring Boot
Redis
Bucket4j (Token Bucket Algorithm)

Limits each user/IP to 2 requests per minute.

⚙️ How It Works
Each user gets a token bucket
Request → consumes 1 token
Tokens available → ✅ allowed
No tokens → ❌ 429 Too Many Requests
Tokens refill over time
🧩 Components
RateLimitConfig → Redis setup
RateLimitService → bucket logic
RateLimitInterceptor → checks requests
WebConfig → applies to /api/**
TestController → sample API
🔁 Flow
Client → Interceptor → Redis (Bucket) → Allow / Block
📡 Endpoint
GET /api/test/limited
🚀 Key Idea

👉 Each user has a bucket → request uses token → no token = blocked
