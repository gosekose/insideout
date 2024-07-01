package com.insideout.usecase.file

import com.insideout.cache.CacheableAnnotation
import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort
import org.springframework.stereotype.Service

@Service
class GenerateFileDownloadPresignedUrlService(
    private val fileManagementPort: FileManagementPort,
) : GenerateFileDownloadPresignedUrlUseCase {
    @CacheableAnnotation("presignedUrlDownloadCache", key = "#command.fileId", durationMillis = 1000 * 60 * 9L)
    override fun generateFileDownloadPresignedUrl(command: GenerateFileDownloadPresignedUrlUseCase.Command): PresignedUrl {
        return fileManagementPort.generateFileDownloadPresignedUrl(
            fileKey = command.fileId.toString(),
            durationMillis = DURATION_MILLIS,
        )
    }

    companion object {
        private const val DURATION_MILLIS = 1000 * 60 * 10L // 10분
    }
}
