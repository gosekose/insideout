package com.insideout.s3

import com.insideout.model.file.FileMetadata
import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort
import org.springframework.boot.context.properties.EnableConfigurationProperties
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@EnableConfigurationProperties(AwsS3Properties::class)
class FileManagementS3Adapter(
    private val s3Presigner: S3Presigner,
    private val awsS3Properties: AwsS3Properties,
) : FileManagementPort {
    override fun generateFileUploadPresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        val putObjectRequest =
            PutObjectRequest.builder()
                .bucket(awsS3Properties.s3.bucket)
                .key(fileKey)
                .build()

        val presignRequest =
            PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMillis(durationMillis))
                .build()

        val presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest)
        return PresignedUrl(
            url = presignedPutObjectRequest.url().toString(),
            fileKey = fileKey,
            vendor = FileMetadata.Vendor.S3,
        )
    }

    override fun generateFileDownloadPresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        val getObjectRequest =
            GetObjectRequest.builder()
                .bucket(awsS3Properties.s3.bucket)
                .key(fileKey)
                .build()

        val presignRequest =
            GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMillis(durationMillis))
                .build()

        val presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest)
        return PresignedUrl(
            url = presignedGetObjectRequest.url().toString(),
            fileKey = fileKey,
            vendor = FileMetadata.Vendor.S3,
        )
    }
}
