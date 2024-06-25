package com.insideout.usecase.member

import com.insideout.model.member.Member
import com.insideout.model.notnull
import com.insideout.usecase.member.port.MemberReader
import com.insideout.usecase.member.port.MemberSaver
import org.springframework.stereotype.Service

@Service
class RegisterEmailV1Service(
    private val memberSaver: MemberSaver,
    private val memberReader: MemberReader,
) : RegisterEmailUseCase {
    override fun execute(registerEmailCommand: RegisterEmailUseCase.RegisterEmailCommand): Member {
        val (memberId, email) = registerEmailCommand

        val asIsMember = memberReader.getByIdOrNull(memberId).notnull()

        if (asIsMember.email != null && asIsMember.email == email) return asIsMember
        if (memberReader.isExistEmail(email)) {
            throw IllegalArgumentException()
        }

        val toBeMember =
            when (asIsMember.version) {
                Member.Version.VERSION_V1 -> {
                    val memberV1 = asIsMember as Member.V1
                    memberV1.registerEmail(email)
                }
            }
        return memberSaver.save(toBeMember)
    }
}
