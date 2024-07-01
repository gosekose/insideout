package com.insideout.usecase.file.port

import com.insideout.model.file.FileMetadata
import com.insideout.model.file.PresignedUrl

interface FileManagementPort {
    fun generateFileUploadPresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl

    fun generateFileDownloadPresignedUrl(
        fileKey: String,
        vendor: FileMetadata.Vendor,
        durationMillis: Long,
    ): PresignedUrl
}
