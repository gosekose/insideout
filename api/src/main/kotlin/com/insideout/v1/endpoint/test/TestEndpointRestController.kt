package com.insideout.v1.endpoint.test

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class TestEndpointRestController {
    @GetMapping(
        value = ["/api/v1/test"],
    )
    fun doGetTest(
        @RequestHeader memberId: Long,
    ) {
        println(memberId)
    }

    @PostMapping(
        value = ["/api/v1/test"],
    )
    fun doPostTest(
        @RequestHeader memberId: Long,
        @RequestBody request: HttpRequest,
    ) {
        println(memberId)
        println(request.id)
    }

    data class HttpRequest(
        @JsonProperty("id") val id: Long,
    )
}
