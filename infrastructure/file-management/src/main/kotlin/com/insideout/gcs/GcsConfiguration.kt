package com.insideout.gcs

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream

@Configuration
@EnableConfigurationProperties(GcsProperties::class)
class GcsConfiguration(
    private val gcsProperties: GcsProperties,
) {
    @Bean
    fun gcsStorage(): Storage {
        val credentials =
            ServiceAccountCredentials
                .fromStream(ByteArrayInputStream(gcsProperties.credentials.json.toByteArray()))
                .createScoped(
                    listOf(
                        "https://www.googleapis.com/auth/cloud-platform",
                        "https://www.googleapis.com/auth/iam",
                    ),
                )
        return StorageOptions.newBuilder()
            .setCredentials(credentials)
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
