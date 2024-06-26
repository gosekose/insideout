package com.insideout.security.manager

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import com.insideout.usecase.memoryMarble.IsExistMemoryMarbleOfMemberUseCase
import com.insideout.util.extension.getMemberId
import com.insideout.util.extension.getPathVariable
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import java.util.function.Supplier

class MemoryMarbleAuthorizationManager(
    private val isExistMemoryMarbleOfMemberUseCase: IsExistMemoryMarbleOfMemberUseCase,
) : AuthorizationManager<RequestAuthorizationContext> {
    override fun check(
        authentication: Supplier<Authentication>,
        context: RequestAuthorizationContext,
    ): AuthorizationDecision {
        val memoryMarbleId =
            context.request.getPathVariable(
                name = NAME,
                pattern = MEMORY_MARBLE_URI_PATTERN,
            ).toLong()

        val memberId = context.request.getMemberId()

        return isAuthorized(memoryMarbleId = memoryMarbleId, memberId = memberId)
    }

    private fun isAuthorized(
        memoryMarbleId: Long,
        memberId: Long,
    ): AuthorizationDecision {
        val result =
            isExistMemoryMarbleOfMemberUseCase.exist(
                memoryMarbleId = memoryMarbleId,
                memberId = memberId,
            )

        if (!result) throw ApplicationBusinessException(BusinessErrorCause.UNAUTHORIZED)

        return AuthorizationDecision(true)
    }

    companion object {
        private const val NAME = "memoryMarbleId"
        const val MEMORY_MARBLE_URI_PATTERN = "/api/**/memoryMarbles/{memoryMarbleId}"
    }
}
