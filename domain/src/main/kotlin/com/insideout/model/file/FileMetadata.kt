package com.insideout.model.file

import com.insideout.model.BaseDomainModel

data class FileMetadata(
    override val id: Long = 0L,
    val memberId: Long,
    val originalFileName: String,
    var vendor: Vendor?,
) : BaseDomainModel() {
    val fileKey = id.toString()

    fun registerVendor(vendor: Vendor): FileMetadata {
        return this.apply {
            this.vendor = vendor
        }
    }

    enum class Vendor {
        S3,
        GCS,
    }

    companion object {
        @JvmStatic
        fun of(
            memberId: Long,
            originalFileName: String,
        ): FileMetadata {
            return FileMetadata(
                memberId = memberId,
                originalFileName = originalFileName,
                vendor = null,
            )
        }
    }
}
