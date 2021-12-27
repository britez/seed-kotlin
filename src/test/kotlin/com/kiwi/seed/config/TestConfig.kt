package com.kiwi.seed.config

import com.kiwi.seed.adapter.rest.handler.RestTemplateErrorHandler
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.sleuth.Tracer
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import java.time.Duration


@TestConfiguration
class TestConfig {

    @Bean
    fun getRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        @Value("\${rest.client.default.timeout}") timeout: Int
    ): RestTemplate {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(timeout.toLong()))
            .setReadTimeout(Duration.ofMillis(timeout.toLong()))
            .errorHandler(RestTemplateErrorHandler(exceptionsMap = emptyMap()))
            .build()
    }

    @Bean
    fun getTracer(): Tracer = Mockito.mock(Tracer::class.java)

}