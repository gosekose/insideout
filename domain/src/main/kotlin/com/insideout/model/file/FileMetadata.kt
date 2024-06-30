package com.insideout.model.file

import com.insideout.model.BaseDomainModel

data class FileMetadata(
    override val id: Long,
    val memberId: Long,
    val originalFileName: String,
    var vendor: Vendor?,
) : BaseDomainModel() {
    val fileKey = id.toString()

    enum class Vendor {
        S3,
        GCS,
    }
}
