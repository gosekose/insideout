package com.insideout.security

import com.insideout.usecase.member.CreateMemberV1UseCase
import com.insideout.usecase.member.GetMemberV1UseCase
import com.insideout.usecase.member.port.TokenPort
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(LoginSecretKeyProperties::class)
class SecurityConfiguration(
    private val tokenPort: TokenPort,
    private val getMemberV1UseCase: GetMemberV1UseCase,
    private val createMemberV1UseCase: CreateMemberV1UseCase,
    private val loginSecretKeyProperties: LoginSecretKeyProperties,
) {
    @Order(1)
    @Bean
    fun apiSecurityFilterChain(
        http: HttpSecurity,
        entryPoint: AuthenticationEntryPoint,
    ): SecurityFilterChain {
        return http
            .securityMatcher(AntPathRequestMatcher("/api/**"))
            .cors {}
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .addFilterBefore(
                corsFilter(),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .authorizeHttpRequests {
                it.requestMatchers(
                    CorsUtils::isPreFlightRequest,
                    AntPathRequestMatcher("/error"),
                    AntPathRequestMatcher("/login"),
                ).permitAll()
            }
            .build()
    }

    @Order(2)
    @Bean
    fun loginSecurityFilterChain(
        http: HttpSecurity,
        entryPoint: AuthenticationEntryPoint,
    ): SecurityFilterChain {
        return http
            .securityMatcher(AntPathRequestMatcher("/api/**/login", "POST"))
            .cors {}
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .securityContext { it.requireExplicitSave(true) }
            .exceptionHandling { it.authenticationEntryPoint(entryPoint) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .build()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val configuration =
            CorsConfiguration().apply {
                allowedOriginPatterns =
                    listOf(
                        "http://localhost:3000",
                    )
                allowedMethods = HttpMethod.values().map { it.name() }.toList()
                allowedHeaders =
                    listOf(
                        HttpHeaders.SET_COOKIE,
                        HttpHeaders.CONTENT_TYPE,
                        HttpHeaders.ACCEPT,
                        HttpHeaders.AUTHORIZATION,
                    )
                allowCredentials = true
            }

        val source =
            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**", configuration)
            }

        return CorsFilter(source)
    }

    @Bean
    fun loginV1AuthenticationFilter(
        authenticationManager: AuthenticationManager,
        delegatingSecurityContextRepository: DelegatingSecurityContextRepository,
    ): LoginV1AuthenticationFilter {
        val filter =
            LoginV1AuthenticationFilter(
                tokenPort = tokenPort,
                getMemberV1UseCase = getMemberV1UseCase,
                createMemberV1UseCase = createMemberV1UseCase,
                loginSecretKeyProperties = loginSecretKeyProperties,
                authenticationManager = authenticationManager,
            )

        filter.setAuthenticationManager(authenticationManager)
        filter.setSecurityContextRepository(delegatingSecurityContextRepository)
        return filter
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun delegateSecurityContextRepository(): DelegatingSecurityContextRepository {
        return DelegatingSecurityContextRepository(
            RequestAttributeSecurityContextRepository(),
        )
    }
}
