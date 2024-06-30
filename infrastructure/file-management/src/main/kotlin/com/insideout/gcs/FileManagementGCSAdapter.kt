package com.insideout.gcs

import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.HttpMethod
import com.google.cloud.storage.Storage
import com.insideout.model.file.FileMetadata
import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort
import org.springframework.boot.context.properties.EnableConfigurationProperties
import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(GcsProperties::class)
class FileManagementGCSAdapter(
    private val storage: Storage,
    private val gcsProperties: GcsProperties,
) : FileManagementPort {
    override fun generatePresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl {
        val blobInfo = BlobInfo.newBuilder(gcsProperties.bucket, fileKey).build()

        val url =
            storage.signUrl(
                blobInfo,
                durationMillis,
                TimeUnit.MILLISECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature(),
            )

        return PresignedUrl(
            url = url.toString(),
            fileKey = fileKey,
            vendor = FileMetadata.Vendor.GCS,
        )
    }
}
