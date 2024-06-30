package com.insideout.configuration

import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigurationProperties
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4jBulkheadProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableConfigurationProperties(Resilience4JConfigurationProperties::class)
class Resilience4JConfiguration(
    private val resilience4JConfigurationProperties: Resilience4JConfigurationProperties,
) {
    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val circuitBreakerConfig =
            CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .build()

        return CircuitBreakerRegistry.of(circuitBreakerConfig)
    }

    @Bean
    fun retryRegistry(): RetryRegistry {
        val retryConfig =
            RetryConfig.custom<RetryConfig>()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(1000))
                .build()

        return RetryRegistry.of(retryConfig)
    }

    @Bean
    fun timeLimiterRegistry(): TimeLimiterRegistry {
        return TimeLimiterRegistry.ofDefaults()
    }

    @Bean
    fun circuitBreakerFactory(): Resilience4JCircuitBreakerFactory {
        return Resilience4JCircuitBreakerFactory(
            circuitBreakerRegistry(),
            timeLimiterRegistry(),
            resilience4jBulkheadProvider(),
        )
    }

    @Bean
    fun bulkheadRegistry(): BulkheadRegistry {
        return BulkheadRegistry.ofDefaults()
    }

    @Bean
    fun threadPollBulkheadRegistry(): ThreadPoolBulkheadRegistry {
        return ThreadPoolBulkheadRegistry.ofDefaults()
    }

    @Bean
    fun resilience4jBulkheadProvider(): Resilience4jBulkheadProvider {
        return Resilience4jBulkheadProvider(
            threadPollBulkheadRegistry(),
            bulkheadRegistry(),
            resilience4JConfigurationProperties,
        )
    }
}
