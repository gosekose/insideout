package com.insideout.v1.endpoint.file.get

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GenerateFileUploadPresignedUrlRestController {
    @GetMapping("/api/v1/files/presignedUrl")
    fun generatePresignedUrl() {

    }
}