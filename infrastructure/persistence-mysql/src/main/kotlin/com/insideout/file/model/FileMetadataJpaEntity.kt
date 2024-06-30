package com.insideout.file.model

import com.insideout.base.BaseJpaEntity
import com.insideout.base.SoftDeleteStatus
import com.insideout.base.applyWithDomainModel
import com.insideout.base.applyWithEntity
import com.insideout.model.file.FileMetadata
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "file_metadatas",
    indexes = [
        Index(name = "idx_file_matadatas__created_at", columnList = "created_at"),
        Index(name = "idx_file_matadatas__member_id", columnList = "member_id"),
    ],
)
class FileMetadataJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "member_id", columnDefinition = "bigint", nullable = false)
    val memberId: Long,
    @Column(name = "original_file_name", columnDefinition = "varchar(255)", nullable = false)
    val originalFileName: String,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "vendor", columnDefinition = "varchar(32)", nullable = true)
    var vendor: FileMetadata.Vendor?,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(32)", nullable = false)
    var status: SoftDeleteStatus,
) : BaseJpaEntity() {
    fun toModel(): FileMetadata {
        return FileMetadata(
            id = id,
            memberId = memberId,
            originalFileName = originalFileName,
            vendor = vendor,
        ).applyWithEntity(this)
    }

    companion object {
        @JvmStatic
        fun from(fileMetadata: FileMetadata): FileMetadataJpaEntity {
            return with(fileMetadata) {
                FileMetadataJpaEntity(
                    id = id,
                    memberId = memberId,
                    originalFileName = originalFileName,
                    vendor = vendor,
                    status = SoftDeleteStatus.ACTIVE,
                )
            }.applyWithDomainModel(fileMetadata)
        }
    }
}
