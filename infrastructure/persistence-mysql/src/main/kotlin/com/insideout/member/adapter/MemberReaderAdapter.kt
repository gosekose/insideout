package com.insideout.member.adapter

import com.insideout.annotation.ReadOnlyTransactional
import com.insideout.member.repository.MemberJpaRepository
import com.insideout.model.member.Member
import com.insideout.model.member.model.Email
import com.insideout.usecase.member.port.MemberReader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
@ReadOnlyTransactional
class MemberReaderAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberReader {
    override fun getByIdOrNull(id: Long): Member? {
        return memberJpaRepository.findByIdOrNull(id)?.toModel()
    }

    override fun isExistEmail(email: Email): Boolean {
        return memberJpaRepository.existsByEmail(email = email.email)
    }
}
