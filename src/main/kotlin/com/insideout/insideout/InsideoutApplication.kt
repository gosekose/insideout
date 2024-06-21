package com.insideout.insideout

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InsideoutApplication

fun main(args: Array<String>) {
    runApplication<InsideoutApplication>(*args)
}
