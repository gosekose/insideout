package com.insideout.v1.endpoint.attachment.get

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateAttachmentPresignedUrlRestController {
    @GetMapping("/api/v1/attachments/presignedUrl")
    fun createPresignedUrl() {

    }
}