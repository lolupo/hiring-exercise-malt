package com.malt.hiringexercise.service

import com.malt.hiringexercise.api.SearchCriteria
import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class SearchCommissionRate(private val commissionRateRepository: CommissionRateRepository) {

    fun searchRateByCriteria(searchCriteria: SearchCriteria): Double {
        // Extract the numeric value from the mission length
        val missionLength = extractNumericValue(searchCriteria.mission.length)
        // Calculate the duration in months between the first and last mission
        val commercialRelationshipDuration = calculateDurationInMonths(
            searchCriteria.commercialRelationship.firstMission,
            searchCriteria.commercialRelationship.lastMission
        )

        val commissionRates = commissionRateRepository.findByRestrictionsCountry("ES")

        // Filter the commission rates by the restrictions
        val filteredRates = commissionRates.filter { commissionRate ->
            commissionRate.restrictions.or.any { restriction ->
                restriction["mission_duration"]?.get("\$gt")?.let {
                    extractNumericValue(it) <= missionLength
                } ?: restriction["commercial_relationship_duration"]?.get("\$gt")?.let {
                    extractNumericValue(it) <= commercialRelationshipDuration
                } ?: false
            }
        }

        return filteredRates.firstOrNull()?.rate?.toDouble() ?: 0.0
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