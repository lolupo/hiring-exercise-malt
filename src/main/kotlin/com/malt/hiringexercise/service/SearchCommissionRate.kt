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

    fun execute(
        searchCriteria: SearchCriteria
    ): Response {

        val commissionRates = commissionRateRepository.findAll()

        return if (commissionRates.isEmpty()) {
            Response(10.0)
        } else {
            val commissionRate = commissionRates.firstOrNull { rate ->
                validateRestrictions(rate.restrictions, searchCriteria)
            }
            if (commissionRate != null) {
                Response(commissionRate.rate, commissionRate.name)
            } else {
                Response(10.0)
            }
        }
    }

    fun validateRestrictions(restrictions: String, searchCriteria: SearchCriteria): Boolean {
        val objectMapper = ObjectMapper()
        val restrictionsNode: JsonNode = objectMapper.readTree(restrictions)

        val orConditions = restrictionsNode["@or"]?.takeIf { it.isArray }
        val andConditions = restrictionsNode["@and"]?.takeIf { it.isArray }
        val clientLocation = restrictionsNode["@client.location"]?.get("country")?.asText() ?: return false
        val freelancerLocation = restrictionsNode["@freelancer.location"]?.get("country")?.asText() ?: return false

        // Validate @or conditions if present
        val orValid = orConditions?.any { condition ->
            condition["mission_duration"]?.get("gt")?.asText()?.let {
                extractNumericValue(it) <= extractNumericValue(searchCriteria.mission.length)
            } ?: condition["commercial_relationship_duration"]?.get("gt")?.asText()?.let {
                extractNumericValue(it) <= calculateDurationInMonths(
                    searchCriteria.commercialRelationship.firstMission,
                    searchCriteria.commercialRelationship.lastMission
                )
            } ?: false
        } ?: true

        // Validate @and conditions if present
        val andValid = andConditions?.all { condition ->
            condition["mission_duration"]?.get("gt")?.asText()?.let {
                extractNumericValue(it) <= extractNumericValue(searchCriteria.mission.length)
            } ?: condition["commercial_relationship_duration"]?.get("gt")?.asText()?.let {
                extractNumericValue(it) <= calculateDurationInMonths(
                    searchCriteria.commercialRelationship.firstMission,
                    searchCriteria.commercialRelationship.lastMission
                )
            } ?: false
        } ?: true

        // Validate @client.location and @freelancer.location
        val clientLocationValid = clientLocation == getIpLocationService.execute(searchCriteria.client.ip)
        val freelancerLocationValid = freelancerLocation == getIpLocationService.execute(searchCriteria.freelancer.ip)

        return orValid && andValid && clientLocationValid && freelancerLocationValid
    }


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