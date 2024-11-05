package com.malt.hiringexercise.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.RequestHeadersUriSpec
import org.springframework.web.client.RestClient.ResponseSpec

@ExtendWith(MockitoExtension::class)
class GetLocationTest {

    @Mock
    private lateinit var restClient: RestClient

    @Mock
    private lateinit var requestHeadersUriSpec: RequestHeadersUriSpec<*>

    @Mock
    private lateinit var responseSpec: ResponseSpec

    @InjectMocks
    private lateinit var getIpLocation: GetIpLocation

    @Test
    fun `should return country code`() {
        val ip = "217.127.206.227"
        val response = """
            {
                "ip": "217.127.206.227",
                "country_code": "ES"
            }
        """

        ReflectionTestUtils.setField(getIpLocation, "accessKey", "test_key")

        Mockito.`when`(restClient.get()).thenReturn(requestHeadersUriSpec)
        Mockito.`when`(requestHeadersUriSpec.uri("/$ip?access_key=test_key")).thenReturn(requestHeadersUriSpec)
        Mockito.`when`(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec)
        Mockito.`when`(responseSpec.body(String::class.java)).thenReturn(response)

        val result = getIpLocation.execute(ip)
        assertEquals("ES", result)
    }

    @Test
    fun `should throw IllegalArgumentException when error occurs`() {
        val ip = "217.127.206.227"

        ReflectionTestUtils.setField(getIpLocation, "accessKey", "test_key")

        Mockito.`when`(restClient.get()).thenReturn(requestHeadersUriSpec)
        Mockito.`when`(requestHeadersUriSpec.uri("/$ip?access_key=test_key")).thenReturn(requestHeadersUriSpec)
        Mockito.`when`(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec)
        Mockito.`when`(responseSpec.body(String::class.java)).thenThrow(RuntimeException::class.java)

        val exception = assertThrows<IllegalArgumentException> {
            getIpLocation.execute(ip)
        }

        assertEquals("Error while fetching ip details", exception.message)
    }
}