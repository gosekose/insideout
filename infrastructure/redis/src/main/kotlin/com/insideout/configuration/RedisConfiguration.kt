package com.insideout.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableConfigurationProperties(RedisConfigurationProperties::class)
class RedisConfiguration(
    private val redisConfigurationProperties: RedisConfigurationProperties,
) {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val (host, port) = redisConfigurationProperties
        return LettuceConnectionFactory(host, port)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *> {
        return RedisTemplate<Any, Any>().apply {
            this.connectionFactory = redisConnectionFactory()
            this.keySerializer = StringRedisSerializer()
            this.hashKeySerializer = StringRedisSerializer()
            this.valueSerializer = StringRedisSerializer()
        }
    }

    @Bean
    fun cacheManager(): CacheManager {
        val defaultCacheConfiguration =
            RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()),
                )
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()),
                )

        val cacheConfigurations =
            mapOf(
                "presignedUrlDownloadCache" to defaultCacheConfiguration.entryTtl(Duration.ofMillis(1000 * 60 * 9)),
            )

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .withInitialCacheConfigurations(cacheConfigurations)
            .cacheDefaults(defaultCacheConfiguration)
            .build()
    }
}
