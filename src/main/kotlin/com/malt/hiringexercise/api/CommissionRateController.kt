package com.malt.hiringexercise.api

import com.malt.hiringexercise.service.IpStackService
import com.malt.hiringexercise.service.SearchCommissionRate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/commission-rate")
class DummyController(
    private val searchCommissionRate: SearchCommissionRate,
    private val ipStackService: IpStackService
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

        val clientLocation = ipStackService.getIpDetails(searchCriteria.client.ip)
        val freelancerLocation = ipStackService.getIpDetails(searchCriteria.freelancer.ip)

        return clientLocation.takeIf { it == freelancerLocation }
            ?.let { searchCommissionRate.searchRateByCriteria(searchCriteria, it) }
            ?: throw IllegalArgumentException("Client and freelancer are not in the same country")
    }
}