package com.malt.hiringexercise.api

import com.malt.hiringexercise.service.SearchCommissionRate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/commission-rate")
class DummyController(
    private val service: SearchCommissionRate
) {

    @PostMapping("/rate")
    @Operation(summary = "Get Commission rate by search criteria")
    @ApiResponse(
        responseCode = "200",
        description = "The Commission rate value",
        content = [Content(schema = Schema(type = "number", format = "double"))]
    )
    fun searchCommissionRate(
        @RequestBody
        searchCriteria: SearchCriteria
    ): Double {
        return service.searchRateByCriteria(searchCriteria)
    }
}