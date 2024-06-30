package com.insideout.v1.endpoint.objectField

import com.insideout.model.file.PresignedUrl

data class PresignedUrlMetadataHttpResponse(
    val fileId: Long,
    val url: String,
) {
    companion object {
        @JvmStatic
        fun from(presignedUrl: PresignedUrl): PresignedUrlMetadataHttpResponse {
            return with(presignedUrl) {
                PresignedUrlMetadataHttpResponse(
                    fileId = fileId,
                    url = url,
                )
            }
        }
    }
}
