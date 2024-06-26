package com.insideout.adapter

import com.insideout.RedisPort
import com.insideout.extension.parseJson
import com.insideout.extension.toJson
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class LettuceRedisAdapter(
    private val redisTemplate: StringRedisTemplate,
) : RedisPort {
    override fun <T> getOrNull(
        key: String,
        type: Class<T>,
    ): T? {
        val value = redisTemplate.opsForValue()[key.redisKey()]
        return value?.parseJson(type)
    }

    override fun set(
        key: String,
        value: Any,
    ) {
        redisTemplate.opsForValue()[key.redisKey()] = value.toJson()
    }

    override fun setIfAbsent(
        key: String,
        value: Any,
    ): Boolean {
        return requireNotNull(redisTemplate.opsForValue().setIfAbsent(key.redisKey(), value.toJson()))
    }

    override fun setIfAbsent(
        key: String,
        value: Any,
        durationMillis: Long,
    ): Boolean {
        return requireNotNull(
            redisTemplate.opsForValue().setIfAbsent(key.redisKey(), value.toJson(), Duration.ofMillis(durationMillis)),
        )
    }

    override fun delete(key: String) {
        redisTemplate.delete(key.redisKey())
    }

    private fun String.redisKey(): String {
        return KEY_PREFIX + this
    }

    companion object {
        private const val KEY_PREFIX = "insideout:"
    }
}
