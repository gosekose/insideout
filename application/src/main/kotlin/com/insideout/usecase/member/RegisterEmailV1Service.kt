package com.insideout.usecase.member

import com.insideout.distributeLock.DistributedLockBeforeTransaction
import com.insideout.distributeLock.DistributedLockPrefixKey
import com.insideout.exception.BusinessErrorCause
import com.insideout.exception.requireBusiness
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
    @DistributedLockBeforeTransaction(
        key = ["#registerEmailCommand.memberId"],
        prefix = DistributedLockPrefixKey.MEMBER_UPDATE_WITH_MEMBER_ID,
        transactionalReadOnly = false,
    )
    override fun execute(registerEmailCommand: RegisterEmailUseCase.RegisterEmailCommand): Member {
        val (memberId, email) = registerEmailCommand

        val asIsMember = memberReader.getByIdOrNull(memberId).notnull(BusinessErrorCause.MEMBER_NOT_FOUND)

        if (asIsMember.email != null && asIsMember.email == email) return asIsMember
        requireBusiness(
            memberReader.isExistEmail(email),
            BusinessErrorCause.EMAIL_ALREADY_EXISTS,
        )

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
