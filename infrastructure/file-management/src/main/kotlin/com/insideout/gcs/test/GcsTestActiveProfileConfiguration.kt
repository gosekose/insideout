package com.insideout.gcs.test

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.insideout.gcs.FileManagementGCSAdapter
import com.insideout.gcs.GcsProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
@EnableConfigurationProperties(GcsProperties::class)
class GcsTestActiveProfileConfiguration(
    private val gcsProperties: GcsProperties,
) {
    @Bean
    fun gcsStorage(): Storage {
        return StorageOptions.newBuilder()
            .setProjectId(gcsProperties.projectId)
            .build()
            .service
    }

    @Qualifier("fileManagementGCSAdapter")
    @Bean
    fun fileManagementGCSAdapter(): FileManagementGCSAdapter {
        return FileManagementGCSAdapter(
            storage = gcsStorage(),
            gcsProperties = gcsProperties,
        )
    }
}
