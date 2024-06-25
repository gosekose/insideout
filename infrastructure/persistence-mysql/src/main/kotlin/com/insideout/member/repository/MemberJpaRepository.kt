package com.insideout.member.repository

import com.insideout.member.model.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {
    fun existsByEmail(email: String): Boolean
}
