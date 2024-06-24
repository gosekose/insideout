package com.insideout.usecase.member

import com.insideout.model.member.model.Member

interface CreateMemberUseCase {
    fun execute(): Member
}
