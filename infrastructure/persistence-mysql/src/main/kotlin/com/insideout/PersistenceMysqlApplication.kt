package com.insideout

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersistenceMysqlApplication

fun main(args: Array<String>) {
    runApplication<PersistenceMysqlApplication>(*args)
}
