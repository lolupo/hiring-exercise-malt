package com.malt.hiringexercise

import com.malt.hiringexercise.api.dto.*
import com.malt.hiringexercise.domain.model.CommissionRate
import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private lateinit var restTemplate: TestRestTemplate

	@Autowired
	private lateinit var commissionRateRepository: CommissionRateRepository

	@Test
	fun `test add and search commission rate`() {
		// Clear the repository
		commissionRateRepository.deleteAll()

		// Add a new commission rate
		val commissionRate = CommissionRate(
			name = "Standard",
			rate = 10.0,
			restrictions = "{\"@and\": [{\"mission_duration\": {\"gt\": \"2months\"}}, {\"commercial_relationship_duration\": {\"gt\": \"2months\"}}], \"@client.location\": {\"country\": \"ES\"}, \"@freelancer.location\": {\"country\": \"ES\"}}"
		)
		val headers = HttpHeaders()
		headers.set("Content-Type", "application/json")
		val request = HttpEntity(commissionRate, headers)
		val addResponse: ResponseEntity<CommissionRate> = restTemplate.postForEntity("/v1/commission-rate/add", request, CommissionRate::class.java)
		assertEquals(HttpStatus.CREATED, addResponse.statusCode)

		// Search for the commission rate
		val searchCriteria = SearchCriteria(
			client = Client(ip = "192.168.1.1"),
			freelancer = Freelancer(ip = "192.168.1.2"),
			mission = Mission(length = "4months"),
			commercialRelationship = CommercialRelationship(
				firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
				lastMission = LocalDateTime.of(2022, 5, 1, 0, 0)
			)
		)
		val searchRequest = HttpEntity(searchCriteria, headers)
		val searchResponse: ResponseEntity<Response> = restTemplate.exchange("/v1/commission-rate/rate", HttpMethod.POST, searchRequest, Response::class.java)
		assertEquals(HttpStatus.OK, searchResponse.statusCode)
		assertEquals(10.0, searchResponse.body?.fees)
	}
}