package com.insideout.usecase.file

import com.insideout.model.file.FileMetadata
import com.insideout.model.file.PresignedUrl
import com.insideout.usecase.file.port.FileManagementPort
import com.insideout.usecase.file.port.FileMetadataSaver
import org.springframework.stereotype.Service

@Service
class GenerateFileUploadPresignedUrlService(
    private val fileMetadataSaver: FileMetadataSaver,
    private val fileManagementPort: FileManagementPort,
) : GenerateFileUploadPresignedUrlUseCase {
    override fun generatePresignedUrl(command: GenerateFileUploadPresignedUrlUseCase.Command): PresignedUrl {
        val (memberId, fileName) = command
        val fileMetadata = fileMetadataSaver.save(FileMetadata.of(memberId, fileName))

        val presignedUrl =
            fileManagementPort.generatePresignedUrl(
                fileKey = fileMetadata.fileKey,
                durationMillis = DURATION_MILLIS,
            )

        fileMetadataSaver.save(fileMetadata.registerVendor(presignedUrl.vendor))

        return presignedUrl
    }

    companion object {
        private const val DURATION_MILLIS = 1000 * 60 * 10L // 10분
    }
}
