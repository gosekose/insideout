package com.insideout

import jakarta.annotation.PostConstruct
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.time.ZoneOffset
import java.util.TimeZone
import kotlin.system.exitProcess

@SpringBootApplication
class BatchApplication {
    @PostConstruct
    fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}

fun main(args: Array<String>) {
    exitProcess(
        SpringApplication.exit(
            SpringApplicationBuilder(BatchApplication::class.java)
                .run(*args),
        ),
    )
}
