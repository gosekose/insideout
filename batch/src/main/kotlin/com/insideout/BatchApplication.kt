package com.insideout

import jakarta.annotation.PostConstruct
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.time.ZoneOffset
import java.util.TimeZone

@SpringBootApplication
class BatchApplication {
    @PostConstruct
    fun initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(BatchApplication::class.java, *args)
}
