package com.insideout.usecase.file

import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort
import org.springframework.stereotype.Service

@Service
class GeneratePresignedUrlService(
    private val fileManagementPort: FileManagementPort,
) : GeneratePresignedUrlUseCase {
    override fun generatePresignedUrl(memberId: Long, fileName: String): PresignedUrl {
        TODO("Not yet implemented")
    }
}