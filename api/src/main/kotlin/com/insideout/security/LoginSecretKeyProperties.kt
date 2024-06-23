package com.insideout.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class LoginSecretKeyProperties(
    val secretKey: String,
)
