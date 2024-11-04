package com.malt.hiringexercise.service

import com.malt.hiringexercise.api.dto.*
import com.malt.hiringexercise.domain.model.CommissionRate
import com.malt.hiringexercise.domain.model.Restrictions
import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class SearchCommissionRateTest {

    @Mock
    private lateinit var commissionRateRepository: CommissionRateRepository

    @InjectMocks
    private lateinit var searchCommissionRate: SearchCommissionRate

    private val correctResponse = Response(8.0, "Standard")
    private val unCorrectResponse = Response(10.0)

    @Test
    fun `should return correct commission rate`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "4months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 5, 1, 0, 0)
            )
        )

        val restrictions = Restrictions(
            or = listOf(
                mapOf("mission_duration" to mapOf("\$gt" to "2months")),
                mapOf("commercial_relationship_duration" to mapOf("\$gt" to "2months"))
            ),
            country = "ES"
        )

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findByRestrictionsCountry("ES")).thenReturn(commissionRates)

        val result = searchCommissionRate.execute(searchCriteria, "ES")

        assertEquals(correctResponse, result)
    }

    @Test
    fun `should return correct commission rate when only mission_duration is valid`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "2months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = Restrictions(
            or = listOf(
                mapOf("mission_duration" to mapOf("\$gt" to "2months")),
                mapOf("commercial_relationship_duration" to mapOf("\$gt" to "2months"))
            ),
            country = "ES"
        )

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findByRestrictionsCountry("ES")).thenReturn(commissionRates)

        val result = searchCommissionRate.execute(searchCriteria, "ES")

        assertEquals(correctResponse, result)
    }

    @Test
    fun `should return correct commission rate when only commercial_relationship_duration is valid`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "1months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 5, 1, 0, 0)
            )
        )

        val restrictions = Restrictions(
            or = listOf(
                mapOf("mission_duration" to mapOf("\$gt" to "2months")),
                mapOf("commercial_relationship_duration" to mapOf("\$gt" to "2months"))
            ),
            country = "ES"
        )

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findByRestrictionsCountry("ES")).thenReturn(commissionRates)

        val result = searchCommissionRate.execute(searchCriteria, "ES")

        assertEquals(correctResponse, result)
    }

    @Test
    fun `should return 10 when no restriction is valid`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "1months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = Restrictions(
            or = listOf(
                mapOf("mission_duration" to mapOf("\$gt" to "2months")),
                mapOf("commercial_relationship_duration" to mapOf("\$gt" to "2months"))
            ),
            country = "ES"
        )

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findByRestrictionsCountry("ES")).thenReturn(commissionRates)

        val result = searchCommissionRate.execute(searchCriteria, "ES")

        assertEquals(unCorrectResponse, result)
    }

    @Test
    fun `should return 10 when database returns empty result`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "2months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        Mockito.`when`(commissionRateRepository.findByRestrictionsCountry("ES")).thenReturn(emptyList())

        val result = searchCommissionRate.execute(searchCriteria, "ES")

        assertEquals(unCorrectResponse, result)
    }


}