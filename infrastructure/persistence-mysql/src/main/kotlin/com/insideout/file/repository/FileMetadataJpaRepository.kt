package com.insideout.file.repository

import com.insideout.base.SoftDeleteStatus
import com.insideout.file.model.FileMetadataJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FileMetadataJpaRepository : JpaRepository<FileMetadataJpaEntity, Long> {
    fun findByIdAndStatus(
        id: Long,
        status: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): FileMetadataJpaEntity?

    fun findByIdInAndMemberIdAndStatus(
        ids: List<Long>,
        memberId: Long,
        status: SoftDeleteStatus = SoftDeleteStatus.ACTIVE,
    ): List<FileMetadataJpaEntity>
}
