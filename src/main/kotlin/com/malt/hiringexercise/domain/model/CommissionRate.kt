package com.malt.hiringexercise.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "commission_rate")
data class CommissionRate(
    @Id val id: String? = null,
    val name: String,
    val rate: Double,
    val restrictions: String
)