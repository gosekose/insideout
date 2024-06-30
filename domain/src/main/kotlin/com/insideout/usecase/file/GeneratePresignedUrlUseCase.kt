package com.insideout.usecase.file

import com.insideout.model.file.PresignedUrl

interface GeneratePresignedUrlUseCase {
    fun generatePresignedUrl(memberId: Long, fileName: String): PresignedUrl
}