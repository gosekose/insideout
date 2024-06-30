package com.insideout.usecase.file.port

import com.insideout.model.file.PresignedUrl

interface FileManagementPort {
    fun generatePresignedUrl(
        fileKey: String,
        durationMillis: Long,
    ): PresignedUrl
}
