package com.insideout

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import java.time.ZoneOffset
import java.util.TimeZone

@SpringBootApplication
@EnableAsync
class ApiApplication {
    @PostConstruct
    fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
