package com.insideout.v1.endpoint.objectField

import com.insideout.model.member.Member

data class MemberHttpResponse(
    val email: String?,
) {
    companion object {
        @JvmStatic
        fun from(member: Member): MemberHttpResponse {
            return with(member) {
                MemberHttpResponse(
                    email = email?.email,
                )
            }
        }
    }
}
