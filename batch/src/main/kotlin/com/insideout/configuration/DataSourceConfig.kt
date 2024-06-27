package com.insideout.configuration

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    @Bean(name = ["batchDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource-batch")
    fun batchDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }

    @Bean(name = ["businessDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource")
    fun businessDataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }


    @Bean
    fun dataSource(@Qualifier("batchDataSource") batchDataSource: DataSource): DataSource {
        return batchDataSource
    }
}