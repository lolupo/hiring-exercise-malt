package com.malt.hiringexercise.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class GetIpLocation(private val restClient: RestClient) {

    @Value("\${ipstack.access_key}")
    private lateinit var accessKey: String
    private val objectMapper = ObjectMapper()

    fun execute(ip: String): String {
        val url = "/$ip?access_key=$accessKey"
        try {
            val response = restClient.get()
                .uri(url)
                .retrieve()
                .body(String::class.java)
            val jsonNode: JsonNode = objectMapper.readTree(response)
            return jsonNode["country_code"].asText()
        } catch (e: Exception) {
            throw IllegalArgumentException("Error while fetching ip details", e)
        }
    }
}