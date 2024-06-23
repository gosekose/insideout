package com.insideout.usecase.member

import com.insideout.model.member.model.Member

interface CreateMemberV1UseCase {
    fun execute(): Member
}
