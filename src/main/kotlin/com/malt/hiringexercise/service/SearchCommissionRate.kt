package com.malt.hiringexercise.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.malt.hiringexercise.api.dto.Response
import com.malt.hiringexercise.api.dto.SearchCriteria
import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class SearchCommissionRate(
    private val commissionRateRepository: CommissionRateRepository,
    private val getIpLocationService: GetIpLocation
) {

    private val objectMapper = ObjectMapper()

    fun execute(
        searchCriteria: SearchCriteria
    ): Response {

        val commissionRates = commissionRateRepository.findAll()

        return commissionRates.firstOrNull { rate ->
            validateRestrictions(objectMapper.readTree(rate.restrictions), searchCriteria)
        }?.let { commissionRate ->
            Response(commissionRate.rate, commissionRate.name)
        } ?: Response(10.0)
    }

    fun validateRestrictions(restrictions: JsonNode, searchCriteria: SearchCriteria): Boolean {
        val orConditions = restrictions["@or"]?.takeIf { it.isArray }
        val andConditions = restrictions["@and"]?.takeIf { it.isArray }
        val clientLocation = restrictions["@client.location"]?.get("country")?.asText() ?: return false
        val freelancerLocation = restrictions["@freelancer.location"]?.get("country")?.asText() ?: return false

        val orValid = orConditions?.any { condition ->
            checkConditions(condition, searchCriteria)
        } != false

        val andValid = andConditions?.all { condition ->
            checkConditions(condition, searchCriteria)
        } != false

        val clientLocationValid = clientLocation == getIpLocationService.execute(searchCriteria.client.ip)
        val freelancerLocationValid = freelancerLocation == getIpLocationService.execute(searchCriteria.freelancer.ip)

        return orValid && andValid && clientLocationValid && freelancerLocationValid
    }

    // Check the conditions for the mission and commercial relationship duration
    private fun checkConditions(
        condition: JsonNode?,
        searchCriteria: SearchCriteria
    ): Boolean = (condition?.get("mission_duration")?.get("gt")?.asText()?.let {
        extractNumericValue(it) <= extractNumericValue(searchCriteria.mission.length)
    } ?: condition?.get("commercial_relationship_duration")?.get("gt")?.asText()?.let {
        extractNumericValue(it) <= calculateDurationInMonths(
            searchCriteria.commercialRelationship.firstMission,
            searchCriteria.commercialRelationship.lastMission
        )
    }) == true

    // Extract the numeric value from this kind of string: "4months"
    private fun extractNumericValue(duration: String): Int {
        val regex = "(\\d+)".toRegex()
        val matchResult = regex.find(duration)
        return matchResult?.value?.toInt() ?: 0
    }

    private fun calculateDurationInMonths(start: LocalDateTime, end: LocalDateTime): Int {
        return ChronoUnit.MONTHS.between(start, end).toInt()
    }


}