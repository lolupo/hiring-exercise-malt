package com.malt.hiringexercise.service

import com.malt.hiringexercise.api.dto.*
import com.malt.hiringexercise.domain.model.CommissionRate
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

    @Mock
    private lateinit var ipStackService: IpStackService

    @InjectMocks
    private lateinit var searchCommissionRate: SearchCommissionRate

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

        val restrictions = """
            {
                "@or": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(8.0, "Standard"), result)
    }

    @Test
    fun `should return correct commission rate when one or condition is met`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "4months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = """
            {
                "@or": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(8.0, "Standard"), result)
    }

    @Test
    fun `should return default commission rate when no restriction is valid`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "1months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = """
            {
                "@or": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(10.0), result)
    }

    @Test
    fun `should return default commission rate when no commission rates are found`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "2months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(emptyList())

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(10.0), result)
    }

    @Test
    fun `should return correct commission rate with and condition`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "4months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 5, 1, 0, 0)
            )
        )

        val restrictions = """
            {
                "@and": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(8.0, "Standard"), result)
    }

    @Test
    fun `should return default commission rate when and condition is not met`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "1months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = """
            {
                "@and": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(10.0), result)
    }

    @Test
    fun `should return default commission rate when or condition is not met`() {
        val searchCriteria = SearchCriteria(
            client = Client(ip = "192.168.1.1"),
            freelancer = Freelancer(ip = "192.168.1.2"),
            mission = Mission(length = "1months"),
            commercialRelationship = CommercialRelationship(
                firstMission = LocalDateTime.of(2022, 1, 1, 0, 0),
                lastMission = LocalDateTime.of(2022, 1, 2, 0, 0)
            )
        )

        val restrictions = """
            {
                "@or": [
                    {"mission_duration": {"gt": "2months"}},
                    {"commercial_relationship_duration": {"gt": "2months"}}
                ],
                "@client.location": {"country": "ES"},
                "@freelancer.location": {"country": "ES"}
            }
        """.trimIndent()

        val commissionRates = listOf(
            CommissionRate(name = "Standard", rate = 8.0, restrictions = restrictions)
        )

        Mockito.`when`(commissionRateRepository.findAll()).thenReturn(commissionRates)
        Mockito.`when`(ipStackService.execute("192.168.1.1")).thenReturn("ES")
        Mockito.`when`(ipStackService.execute("192.168.1.2")).thenReturn("ES")

        val result = searchCommissionRate.execute(searchCriteria)

        assertEquals(Response(10.0), result)
    }
}