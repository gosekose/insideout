package com.insideout.usecase.file

import com.insideout.cache.CacheableAnnotation
import com.insideout.exception.BusinessErrorCause
import com.insideout.exception.requireBusiness
import com.insideout.model.file.PresignedUrl
import com.insideout.model.notnull
import com.insideout.usecase.file.port.FileManagementPort
import com.insideout.usecase.file.port.FileMetadataReader
import org.springframework.stereotype.Service

@Service
class GenerateFileDownloadPresignedUrlService(
    private val fileMetadataReader: FileMetadataReader,
    private val fileManagementPort: FileManagementPort,
) : GenerateFileDownloadPresignedUrlUseCase {
    @CacheableAnnotation("presignedUrlDownloadCache", key = "#command.fileId", durationMillis = 1000 * 60 * 9L)
    override fun generateFileDownloadPresignedUrl(command: GenerateFileDownloadPresignedUrlUseCase.Command): PresignedUrl {
        val fileMetadata = fileMetadataReader.getByIdOrNull(command.fileId).notnull()
        val vendor = fileMetadata.vendor
        requireBusiness(vendor != null, BusinessErrorCause.NOT_FOUND)

        return fileManagementPort.generateFileDownloadPresignedUrl(
            fileKey = command.fileId.toString(),
            vendor = vendor,
            durationMillis = DURATION_MILLIS,
        )
    }

    companion object {
        private const val DURATION_MILLIS = 1000 * 60 * 10L // 10분
    }
}
