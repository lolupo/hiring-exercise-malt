package com.malt.hiringexercise.domain.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "commission_rate")
data class CommissionRate (
     val name: String,
     val rate: Double,
     val restrictions: String
    )