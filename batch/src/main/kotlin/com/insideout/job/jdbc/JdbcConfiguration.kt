package com.insideout.job.jdbc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import javax.sql.DataSource

@Configuration
class JdbcConfiguration(private val dataSource: DataSource) {
    @Bean
    fun jdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Bean
    fun simpleJdbcInsert(): SimpleJdbcInsert {
        return SimpleJdbcInsert(dataSource)
    }
}
