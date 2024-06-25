package com.insideout.usecase.member

import com.insideout.model.member.Member
import com.insideout.usecase.member.port.MemberSaver
import org.springframework.stereotype.Service

@Service
class CreateMemberV1Service(
    private val memberSaver: MemberSaver,
) : CreateMemberUseCase {
    override fun execute(): Member {
        return memberSaver.save(Member.V1(email = null))
    }
}
