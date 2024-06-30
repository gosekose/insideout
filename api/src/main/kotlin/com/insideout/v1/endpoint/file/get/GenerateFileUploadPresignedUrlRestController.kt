package com.insideout.v1.endpoint.file.get

import com.insideout.usecase.file.GenerateFileUploadPresignedUrlUseCase
import com.insideout.v1.endpoint.objectField.PresignedUrlMetadataHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class GenerateFileUploadPresignedUrlRestController(
    private val generateFileUploadPresignedUrlUseCase: GenerateFileUploadPresignedUrlUseCase,
) {
    @GetMapping("/api/v1/files/presignedUrl")
    fun generatePresignedUrl(
        @RequestHeader("memberId") memberId: Long,
        @RequestBody request: HttpRequest,
    ): PresignedUrlMetadataHttpResponse {
        return generateFileUploadPresignedUrlUseCase.generatePresignedUrl(request.toCommand(memberId))
            .let(PresignedUrlMetadataHttpResponse::from)
    }

    data class HttpRequest(
        val fileName: String,
    ) {
        fun toCommand(memberId: Long): GenerateFileUploadPresignedUrlUseCase.Command {
            return GenerateFileUploadPresignedUrlUseCase.Command(
                memberId = memberId,
                fileName = fileName,
            )
        }
    }
}
