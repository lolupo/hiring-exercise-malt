package com.malt.hiringexercise.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response containing the calculated fees and the reason for the calculation")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response(
    @field:Schema(description = "spain or repeat", example = "150.0")
    val fees: Double,

    @field:Schema(description = "Reason for the calculated fees", example = "Standard commission rate applied", nullable = true)
    var reason: String? = null
)