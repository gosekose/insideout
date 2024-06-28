package com.insideout.jdbc

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import javax.sql.DataSource

@Configuration
class JdbcConfiguration(
    @Qualifier("businessDataSource") private val dataSource: DataSource,
    @Qualifier("batchDataSource") private val batchDataSource: DataSource,
) {
    @Primary
    @Qualifier("jdbcTemplate")
    @Bean
    fun jdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }

    @Qualifier("batchJdbcTemplate")
    @Bean
    fun batchJdbcTemplate(): JdbcTemplate {
        return JdbcTemplate(batchDataSource)
    }

    @Qualifier("batchSimpleJdbcInsert")
    @Bean
    fun batchSimpleJdbcInsert(): SimpleJdbcInsert {
        return SimpleJdbcInsert(batchDataSource)
    }
}
