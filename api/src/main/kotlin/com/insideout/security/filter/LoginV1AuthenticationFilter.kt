package com.insideout.security.filter

import com.insideout.model.member.Member
import com.insideout.security.authentication.AuthenticationV1Context
import com.insideout.security.properties.LoginSecretKeyProperties
import com.insideout.usecase.member.CreateMemberUseCase
import com.insideout.usecase.member.GetMemberUseCase
import com.insideout.usecase.member.port.TokenPort
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.crypto.SecretKey

@EnableConfigurationProperties(LoginSecretKeyProperties::class)
class LoginV1AuthenticationFilter(
    private val tokenPort: TokenPort,
    private val getMemberUseCase: GetMemberUseCase,
    private val createMemberUseCase: CreateMemberUseCase,
    private val loginSecretKeyProperties: LoginSecretKeyProperties,
    authenticationManager: AuthenticationManager,
) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher("/api/v1/login", "POST")) {
    private lateinit var key: SecretKey

    init {
        super.setAuthenticationManager(authenticationManager)
    }

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        val keyBytes = Decoders.BASE64.decode(loginSecretKeyProperties.secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Authentication {
        val token = request.getHeader(AUTHORIZATION_HEADER)?.let { parseBearer(it) }.orEmpty()
        val member =
            if (token.isBlank()) {
                handleNewLogin(response)
            } else {
                handleExistingLogin(token, response)
            }

        val authenticationToken = UsernamePasswordAuthenticationToken(AuthenticationV1Context.from(member), null)

        return authenticationManager.authenticate(authenticationToken).also {
            SecurityContextHolder.getContext().authentication = it
        }
    }

    private fun handleNewLogin(response: HttpServletResponse): Member {
        return createMemberUseCase.execute().also {
            val newToken =
                tokenPort.create(
                    subject = it.id.toString(),
                    secretKey = key,
                    expirationMillis = EXPIRATION_MILLIS_TIME,
                )
            setAuthorizationHeader(response, newToken)
        }
    }

    private fun handleExistingLogin(
        token: String,
        response: HttpServletResponse,
    ): Member {
        val memberId =
            tokenPort.parse(
                token = token,
                secretKey = key,
                converter = { it.toLong() },
            )
        return runCatching {
            getMemberUseCase.execute(memberId).also {
                setAuthorizationHeader(response, token)
            }
        }.getOrElse {
            handleNewLogin(response)
        }
    }

    private fun setAuthorizationHeader(
        response: HttpServletResponse,
        token: String,
    ) {
        response.setHeader(AUTHORIZATION_HEADER, "$BEARER$token")
    }

    private fun parseBearer(authorization: String): String {
        return authorization.removePrefix(BEARER).trim()
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER = "Bearer "
        private const val EXPIRATION_MILLIS_TIME = 31104000000L // TODO: 추후 시간 단축
    }
}
