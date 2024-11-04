package com.malt.hiringexercise.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    private var url = "http://api.ipstack.com"

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .requestFactory(HttpComponentsClientHttpRequestFactory())
            .baseUrl(url)
            .defaultHeader("Content-Type", "application/json")
            .build()
    }
}