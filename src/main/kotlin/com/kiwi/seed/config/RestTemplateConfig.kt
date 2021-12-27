package com.kiwi.seed.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class RestTemplateConfig {

    @Bean
    fun getRestTemplate(
        restTemplateBuilder: RestTemplateBuilder,
        @Value("\${rest.client.default.timeout}") timeout: Int
    ): RestTemplate {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(timeout.toLong()))
            .setReadTimeout(Duration.ofMillis(timeout.toLong()))
            .errorHandler(DefaultResponseErrorHandler())
            .build()
    }
}