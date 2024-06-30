package com.insideout.adapter

import com.insideout.gcs.FileManagementGCSAdapter
import com.insideout.model.file.PresignedUrl
import com.insideout.s3.FileManagementS3Adapter
import com.insideout.usecase.file.port.FileManagementPort
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary
@Component
class FileManagementAdapter(
    @Qualifier("fileManagementS3Adapter") private val s3Adapter: FileManagementS3Adapter,
    @Qualifier("fileManagementGCSAdapter") private val gcsAdapter: FileManagementGCSAdapter,
    circuitBreakerFactory: CircuitBreakerFactory<*, *>,
) : FileManagementPort {
    private val circuitBreaker = circuitBreakerFactory.create("s3CircuitBreaker")

    override fun generatePresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        return circuitBreaker.run({
            s3Adapter.generatePresignedUrl(fileKey, durationMillis)
        }, {
            gcsAdapter.generatePresignedUrl(fileKey, durationMillis)
        })
    }
}
