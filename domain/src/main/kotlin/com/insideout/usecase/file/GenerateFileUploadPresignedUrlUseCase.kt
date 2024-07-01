package com.insideout.usecase.file

import com.insideout.model.file.PresignedUrl

interface GenerateFileUploadPresignedUrlUseCase {
    fun generateFileUploadPresignedUrl(command: Command): PresignedUrl

    data class Command(
        val memberId: Long,
        val fileName: String,
    )
}
