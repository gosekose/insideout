package com.insideout.configuration

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class EnvironmentLogger : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(EnvironmentLogger::class.java)

    override fun run(vararg args: String?) {
        val env = System.getenv()
        env.forEach { (key, value) ->
            logger.info("$key = $value")
        }
    }
}
