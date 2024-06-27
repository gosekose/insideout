package com.insideout.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class TransactionManagerConfig {

    @Bean(name = ["batchTransactionManager"])
    fun batchTransactionManager(@Qualifier("batchDataSource") batchDataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(batchDataSource)
    }

    @Bean(name = ["businessTransactionManager"])
    fun businessTransactionManager(@Qualifier("businessDataSource") businessDataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(businessDataSource)
    }

    @Bean
    fun transactionManager(@Qualifier("batchTransactionManager") batchTransactionManager: PlatformTransactionManager): PlatformTransactionManager {
        return batchTransactionManager
    }
}