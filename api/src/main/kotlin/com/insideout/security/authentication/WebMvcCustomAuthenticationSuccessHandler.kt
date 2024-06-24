package com.insideout.security.authentication

import com.google.gson.Gson
import com.insideout.envelope.ResponseEnvelope
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class WebMvcCustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val body = emptyMap<String, String>()
        Gson().toJson(ResponseEnvelope.ok(body))
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_OK
    }
}
