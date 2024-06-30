package com.insideout.s3

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cloud.aws")
data class AwsS3Properties(
    val credentials: Credential,
    val region: Region,
    val s3: S3,
) {
    data class Credential(
        val accessKey: String,
        val secretKey: String,
    )

    data class Region(
        val static: String,
    )

    data class S3(
        val bucket: String,
    )
}
