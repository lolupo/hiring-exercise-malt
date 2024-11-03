package com.malt.hiringexercise.service

import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Service

@Service
class SearchCommissionRate (private val commissionRateRepository: CommissionRateRepository) {

    fun findCommissionRateById(ruleId: String): Double {
        return commissionRateRepository.findCommissionRateByName(ruleId).rate.toDouble()
    }





}