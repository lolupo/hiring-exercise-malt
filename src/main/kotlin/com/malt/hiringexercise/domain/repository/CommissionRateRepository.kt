package com.malt.hiringexercise.domain.repository

import com.malt.hiringexercise.domain.model.CommissionRate
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommissionRateRepository : MongoRepository<CommissionRate, String>
