package com.insideout.adapter

import com.insideout.gcs.FileManagementGCSAdapter
import com.insideout.model.file.PresignedUrl
import com.insideout.s3.FileManagementS3Adapter
import com.insideout.usecase.file.port.FileManagementPort
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(FileManagementGCSAdapter::class.java)

    override fun generateFileUploadPresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        return circuitBreaker.run({
            logger.info("s3 Adapter Try")
            s3Adapter.generateFileUploadPresignedUrl(fileKey, durationMillis)
        }, { throwable ->
            logger.error("s3 Adapter Exception = [${throwable.message}]")
            logger.info("gcs Adapter Try")
            gcsAdapter.generateFileUploadPresignedUrl(fileKey, durationMillis)
        })
    }

    override fun generateFileDownloadPresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        return circuitBreaker.run({
            logger.info("s3 Adapter Try")
            s3Adapter.generateFileDownloadPresignedUrl(fileKey, durationMillis)
        }, { throwable ->
            logger.error("s3 Adapter Exception = [${throwable.message}]")
            logger.info("gcs Adapter Try")
            gcsAdapter.generateFileDownloadPresignedUrl(fileKey, durationMillis)
        })
    }
}
