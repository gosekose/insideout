package com.insideout.security.authentication

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        try {
            val id = (authentication.principal as AuthenticationContext).id
            val userDetails = userDetailsService.loadUserByUsername(id)
            return UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities,
            )
        } catch (e: Exception) {
            throw ApplicationBusinessException(BusinessErrorCause.UNAUTHORIZED)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
