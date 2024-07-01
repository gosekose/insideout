package com.insideout.v1.endpoint.file.get

import com.insideout.usecase.file.GenerateFileDownloadPresignedUrlUseCase
import com.insideout.v1.endpoint.objectField.PresignedUrlMetadataHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class GenerateFileDownloadPresignedUrlRestController(
    private val generateFileDownloadPresignedUrlUseCase: GenerateFileDownloadPresignedUrlUseCase,
) {
    @GetMapping("/api/v1/files/{fileId}/presignedUrl/download")
    fun generatePresignedUrl(
        @RequestHeader("memberId") memberId: Long,
        @PathVariable("fileId") fileId: Long,
    ): PresignedUrlMetadataHttpResponse {
        return generateFileDownloadPresignedUrlUseCase.generateFileDownloadPresignedUrl(
            GenerateFileDownloadPresignedUrlUseCase.Command(
                fileId = fileId,
            ),
        ).let(PresignedUrlMetadataHttpResponse::from)
    }
}
