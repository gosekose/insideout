package com.insideout.adapter

import com.insideout.extension.parseJson
import com.insideout.extension.toJson
import com.insideout.port.CacheManagementPort
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisCacheManagementAdapter(
    private val redisTemplate: StringRedisTemplate,
) : CacheManagementPort {
    override fun <T> get(
        key: String,
        type: Class<T>,
    ): T? {
        val value = redisTemplate.opsForValue()[key]
        return value?.parseJson(type)
    }

    override fun <T> set(
        key: String,
        value: T,
        durationMillis: Long,
    ) {
        redisTemplate.opsForValue().set(key, requireNotNull(value).toJson(), Duration.ofMillis(durationMillis))
    }
}
