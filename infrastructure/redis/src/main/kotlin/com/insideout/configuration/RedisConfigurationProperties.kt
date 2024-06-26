package com.insideout.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisConfigurationProperties(
    val host: String,
    val port: Int,
)
