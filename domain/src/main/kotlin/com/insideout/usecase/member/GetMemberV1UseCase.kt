package com.insideout.usecase.member

import com.insideout.model.member.model.Member

interface GetMemberV1UseCase {
    fun execute(id: Long): Member
}
