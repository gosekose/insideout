package com.insideout.memoryMarble.repository

import com.insideout.base.SoftDeleteStatus
import com.insideout.memoryMarble.model.MemoryMarbleJpaEntity
import com.insideout.memoryMarble.model.QMemoryMarbleJpaEntity
import com.insideout.model.memoryMarble.type.StoreType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemoryMarbleJpaRepository : JpaRepository<MemoryMarbleJpaEntity, Long>, MemoryMarbleJpaCustom {
    fun existsByIdGreaterThan(id: Long): Boolean
}

interface MemoryMarbleJpaCustom {
    fun findByLimitSearch(
        memberId: Long,
        storeType: StoreType?,
        lastId: Long,
        size: Long,
    ): List<MemoryMarbleJpaEntity>
}

class MemoryMarbleJpaCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : MemoryMarbleJpaCustom {
    private val qMemoryMarbleJpaEntity = QMemoryMarbleJpaEntity.memoryMarbleJpaEntity

    override fun findByLimitSearch(
        memberId: Long,
        storeType: StoreType?,
        lastId: Long,
        size: Long,
    ): List<MemoryMarbleJpaEntity> {
        return jpaQueryFactory.selectFrom(qMemoryMarbleJpaEntity)
            .where(
                qMemoryMarbleJpaEntity.memberId.eq(memberId),
                qMemoryMarbleJpaEntity.id.gt(lastId),
                storeType?.let {
                    qMemoryMarbleJpaEntity.storeType.eq(storeType)
                },
                qMemoryMarbleJpaEntity.softDeleteStatus.eq(SoftDeleteStatus.ACTIVE),
            )
            .limit(size)
            .orderBy(qMemoryMarbleJpaEntity.id.asc())
            .fetch()
    }
}
