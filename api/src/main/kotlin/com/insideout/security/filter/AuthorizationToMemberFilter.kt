package com.insideout.security.filter

import com.insideout.exception.ApplicationBusinessException
import com.insideout.exception.BusinessErrorCause
import com.insideout.security.authentication.AuthenticationV1Context
import com.insideout.security.properties.LoginSecretKeyProperties
import com.insideout.usecase.member.port.TokenPort
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Collections
import java.util.Enumeration
import javax.crypto.SecretKey

@EnableConfigurationProperties(LoginSecretKeyProperties::class)
class AuthorizationToMemberFilter(
    private val tokenPort: TokenPort,
    private val loginSecretKeyProperties: LoginSecretKeyProperties,
    private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {
    private lateinit var key: SecretKey
    private val antPathMatcher = AntPathMatcher()
    private val whiteList =
        listOf(
            "/error",
            "/api/**/login",
            "/api/**/health",
        )

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        val keyBytes = Decoders.BASE64.decode(loginSecretKeyProperties.secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val requestUri = request.requestURI
        if (isWhiteListed(requestUri)) {
            filterChain.doFilter(request, response)
            return
        }

        val token =
            try {
                val authorizationHeader = request.getHeader(AUTHORIZATION_HEADER)
                parseBearer(authorizationHeader)
            } catch (e: Exception) {
                logger.error { "UnAuthorized Request [URI = ${request.requestURL}, Exception = ${e.message}]" }
                throw ApplicationBusinessException(BusinessErrorCause.UNAUTHORIZED)
            }

        val memberId = tokenPort.parse(token = token, secretKey = key) { it }

        val wrappedRequest =
            object : HttpServletRequestWrapper(request) {
                override fun getHeader(name: String): String {
                    return if (name == MEMBER_ID_HEADER) {
                        memberId
                    } else {
                        super.getHeader(name)
                    }
                }

                override fun getHeaders(name: String): Enumeration<String> {
                    return if (name == MEMBER_ID_HEADER) {
                        Collections.enumeration(listOf(memberId))
                    } else {
                        super.getHeaders(name)
                    }
                }

                override fun getHeaderNames(): Enumeration<String> {
                    val names = super.getHeaderNames().toList() + MEMBER_ID_HEADER
                    return Collections.enumeration(names)
                }
            }

        val authenticationToken = UsernamePasswordAuthenticationToken(AuthenticationV1Context(memberId), null)

        authenticationManager.authenticate(authenticationToken).also {
            SecurityContextHolder.getContext().authentication = it
        }

        filterChain.doFilter(wrappedRequest, response)
    }

    private fun parseBearer(authorization: String): String {
        return authorization.removePrefix(BEARER).trim()
    }

    private fun isWhiteListed(requestUri: String): Boolean {
        return whiteList.any { antPathMatcher.match(it, requestUri) }
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val MEMBER_ID_HEADER = "memberId"
        private const val BEARER = "Bearer "
    }
}
