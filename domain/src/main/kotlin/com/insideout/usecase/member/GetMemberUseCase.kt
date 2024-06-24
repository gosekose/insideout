package com.insideout.usecase.member

import com.insideout.model.member.model.Member

interface GetMemberUseCase {
    fun execute(id: Long): Member
}
