package com.insideout.security.authentication

import com.insideout.model.member.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

interface AuthenticationContext : Serializable, UserDetails {
    val id: String
}

data class AuthenticationV1Context(
    override val id: String,
) : AuthenticationContext {
    companion object {
        @JvmStatic
        fun from(member: Member): AuthenticationContext = AuthenticationV1Context(member.id.toString())
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String {
        return id
    }

    override fun getUsername(): String {
        return id
    }
}
