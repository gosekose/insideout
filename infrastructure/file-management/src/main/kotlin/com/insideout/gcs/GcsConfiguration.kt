package com.insideout.gcs

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
@EnableConfigurationProperties(GcsProperties::class)
class GcsConfiguration(
    private val gcsProperties: GcsProperties,
) {
    @Bean
    fun gcsClient(): Storage {
        val credentials = GoogleCredentials.fromStream(FileInputStream(gcsProperties.credentials.location))
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(gcsProperties.projectId)
            .build()
            .service
    }
}