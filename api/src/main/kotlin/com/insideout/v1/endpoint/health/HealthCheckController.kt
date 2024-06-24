package com.insideout.v1.endpoint.health

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    private val logger = LoggerFactory.getLogger(HealthCheckController::class.java)

    @GetMapping(
        value = ["/api/v1/health"],
    )
    fun check() {
        logger.info("Health Check Finished")
    }
}
