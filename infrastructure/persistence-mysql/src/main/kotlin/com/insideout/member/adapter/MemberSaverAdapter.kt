package com.insideout.member.adapter

import com.insideout.annotation.WritableTransactional
import com.insideout.member.model.MemberJpaEntity
import com.insideout.member.repository.MemberJpaRepository
import com.insideout.model.member.model.Member
import com.insideout.usecase.member.port.MemberSaver
import org.springframework.stereotype.Repository

@Repository
@WritableTransactional
class MemberSaverAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberSaver {
    override fun save(member: Member): Member {
        return memberJpaRepository.save(MemberJpaEntity.from(member)).toModel()
    }
}
