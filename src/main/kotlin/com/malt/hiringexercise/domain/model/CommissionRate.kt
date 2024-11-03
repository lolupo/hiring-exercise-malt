package com.malt.hiringexercise.domain.model

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "commission_rate")
data class CommissionRate (

     val id: String,
     val name: String,
     val rate: Int,
     val restrictions: Restrictions
    )

data class Restrictions(
    @Field("\$or")
    val or: List<Map<String, Map<String, String>>>,
    val country: String
)