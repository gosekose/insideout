package com.insideout.v1.endpoint.member

import com.insideout.model.member.model.Email
import com.insideout.usecase.member.RegisterEmailUseCase
import com.insideout.v1.endpoint.objectField.MemberHttpResponse
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RegisterEmailRestController(
    private val registerEmailUseCase: RegisterEmailUseCase,
) {
    @PutMapping(
        value = ["/api/v1/profiles/email"],
    )
    fun registerEmail(
        @RequestHeader("memberId") memberId: Long,
        @RequestBody request: HttpRequest,
    ): MemberHttpResponse {
        return registerEmailUseCase.execute(request.toRegisterEmail(memberId))
            .let(MemberHttpResponse::from)
    }

    data class HttpRequest(
        val email: String,
    ) {
        fun toRegisterEmail(memberId: Long): RegisterEmailUseCase.RegisterEmailCommand {
            return RegisterEmailUseCase.RegisterEmailCommand(
                memberId = memberId,
                email = Email(email),
            )
        }
    }
}
