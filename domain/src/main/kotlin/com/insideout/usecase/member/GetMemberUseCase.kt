package com.insideout.usecase.member

import com.insideout.model.member.Member

interface GetMemberUseCase {
    fun execute(id: Long): Member
}
