package com.insideout.usecase.member

import com.insideout.model.member.model.Member
import com.insideout.model.notnull
import com.insideout.usecase.member.port.MemberReader
import org.springframework.stereotype.Service

@Service
class GetMemberV1Service(
    private val memberReader: MemberReader,
) : GetMemberV1UseCase {
    override fun execute(id: Long): Member {
        return memberReader.getByIdOrNull(id).notnull()
    }
}
