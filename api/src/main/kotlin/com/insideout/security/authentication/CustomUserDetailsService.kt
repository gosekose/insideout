package com.insideout.security.authentication

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class CustomUserDetailsService : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return AuthenticationV1Context(username)
    }
}
