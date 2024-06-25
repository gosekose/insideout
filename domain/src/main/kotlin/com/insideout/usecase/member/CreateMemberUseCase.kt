package com.insideout.usecase.member

import com.insideout.model.member.Member

interface CreateMemberUseCase {
    fun execute(): Member
}
