package com.malt.hiringexercise.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Search criteria for finding commission rates")
data class SearchCriteria(
    @field:Schema(description = "Client information", required = true, example = "{\"ip\": \"217.127.206.227\"}")
    val client: Client,

    @field:Schema(description = "Freelancer information", required = true, example = "{\"ip\": \"217.127.206.227\"}")
    val freelancer: Freelancer,

    @field:Schema(description = "Mission information", required = true, example = "{\"length\": \"4months\"}")
    val mission: Mission,

    @field:Schema(description = "Commercial relationship details", required = true, example = "{\"firstMission\": \"2018-04-16T13:24:17.510Z\", \"lastMission\": \"2018-07-16T14:24:17.510Z\"}")
    val commercialRelationship: CommercialRelationship
)

@Schema(description = "Client details")
data class Client(
    @field:Schema(description = "IP address of the client", example = "217.127.206.227")
    val ip: String
)

@Schema(description = "Freelancer details")
data class Freelancer(
    @field:Schema(description = "IP address of the freelancer", example = "217.127.206.227")
    val ip: String
)

@Schema(description = "Mission details")
data class Mission(
    @field:Schema(description = "Length of the mission", example = "4months")
    val length: String
)

@Schema(description = "Commercial relationship details")
data class CommercialRelationship(
    @field:Schema(description = "Date of the first mission", example = "2018-04-16T13:24:17.510Z")
    val firstMission: LocalDateTime,

    @field:Schema(description = "Date of the last mission", example = "2018-07-16T14:24:17.510Z")
    val lastMission: LocalDateTime
)