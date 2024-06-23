package com.insideout.memory.repository

import com.insideout.memory.model.MemoryMarbleJpaEntity
import com.insideout.memory.model.QMemoryMarbleJpaEntity
import com.insideout.model.memory.type.StoreType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemoryMarbleJpaRepository : JpaRepository<MemoryMarbleJpaEntity, Long>, MemoryMarbleJpaCustom {
    fun existsByIdGreaterThan(id: Long): Boolean
}

interface MemoryMarbleJpaCustom {
    fun findByOffsetSearch(
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

    override fun findByOffsetSearch(
        memberId: Long,
        storeType: StoreType?,
        lastId: Long,
        size: Long,
    ): List<MemoryMarbleJpaEntity> {
        return jpaQueryFactory.selectFrom(qMemoryMarbleJpaEntity)
            .where(
                qMemoryMarbleJpaEntity.memberId.eq(memberId),
                storeType?.let {
                    qMemoryMarbleJpaEntity.storeType.eq(storeType)
                },
                qMemoryMarbleJpaEntity.id.gt(lastId),
            )
            .offset(size)
            .orderBy(qMemoryMarbleJpaEntity.id.asc())
            .fetch()
    }
}
