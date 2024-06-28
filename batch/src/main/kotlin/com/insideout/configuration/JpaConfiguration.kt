package com.insideout.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["com.insideout.*"],
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager",
)
class JpaConfiguration {
    @Bean(name = ["entityManagerFactory"])
    fun entityManagerFactory(
        @Qualifier("businessDataSource") dataSource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = dataSource
        factory.setPackagesToScan("com.insideout.*")
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()
        return factory
    }
}
