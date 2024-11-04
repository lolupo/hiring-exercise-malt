package com.malt.hiringexercise.service

import com.malt.hiringexercise.domain.model.CommissionRate
import com.malt.hiringexercise.domain.repository.CommissionRateRepository
import org.springframework.stereotype.Service

@Service
class AddCommissionRateService(private val commissionRateRepository: CommissionRateRepository) {

    fun execute(commissionRate: CommissionRate): CommissionRate {
        return commissionRateRepository.save(commissionRate)
    }
}