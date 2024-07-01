package com.insideout.v1.endpoint.file.get

import com.insideout.usecase.file.GenerateFileUploadPresignedUrlUseCase
import com.insideout.v1.endpoint.objectField.PresignedUrlMetadataHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GenerateFileUploadPresignedUrlRestController(
    private val generateFileUploadPresignedUrlUseCase: GenerateFileUploadPresignedUrlUseCase,
) {
    @GetMapping("/api/v1/files/presignedUrl")
    fun generatePresignedUrl(
        @RequestHeader("memberId") memberId: Long,
        @RequestParam fileName: String,
    ): PresignedUrlMetadataHttpResponse {
        return generateFileUploadPresignedUrlUseCase.generatePresignedUrl(
            GenerateFileUploadPresignedUrlUseCase.Command(
                memberId = memberId,
                fileName = fileName,
            ),
        ).let(PresignedUrlMetadataHttpResponse::from)
    }
}
