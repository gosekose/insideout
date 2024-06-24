package com.insideout.v1.endpoint.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class TestEndpointRestController {
    @GetMapping(
        value = ["/api/v1/test"],
    )
    fun doTest(
        @RequestHeader("memberId") memberId: Long,
    ) {
        println(memberId)
    }
}
