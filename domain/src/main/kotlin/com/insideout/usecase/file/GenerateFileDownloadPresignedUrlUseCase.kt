package com.insideout.usecase.file

import com.insideout.model.file.PresignedUrl

interface GenerateFileDownloadPresignedUrlUseCase {
    fun generateFileDownloadPresignedUrl(command: Command): PresignedUrl

    data class Command(
        val fileId: Long,
    )
}
