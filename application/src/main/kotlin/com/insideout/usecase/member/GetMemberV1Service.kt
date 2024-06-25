package com.insideout.usecase.member

import com.insideout.model.member.Member
import com.insideout.model.notnull
import com.insideout.usecase.member.port.MemberReader
import org.springframework.stereotype.Service

@Service
class GetMemberV1Service(
    private val memberReader: MemberReader,
) : GetMemberUseCase {
    override fun execute(id: Long): Member {
        return memberReader.getByIdOrNull(id).notnull()
    }
}
