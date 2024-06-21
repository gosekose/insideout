package com.insideout.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfiguration {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun jpaQueryFactory(): JPAQueryFactory = JPAQueryFactory(entityManager)
}
