package com.malt.hiringexercise.api

import com.malt.hiringexercise.api.dto.Response
import com.malt.hiringexercise.api.dto.SearchCriteria
import com.malt.hiringexercise.domain.model.CommissionRate
import com.malt.hiringexercise.service.AddCommissionRateService
import com.malt.hiringexercise.service.IpStackService
import com.malt.hiringexercise.service.SearchCommissionRate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/commission-rate")
class CommissionRateController(
    private val searchCommissionRate: SearchCommissionRate,
    private val addCommissionRateService: AddCommissionRateService,
    private val ipStackService: IpStackService
) {

    @PostMapping("/rate")
    @Operation(summary = "Get Commission rate by search criteria")
    @ApiResponse(
        responseCode = "200",
        description = "The Commission rate value",
        content = [Content(schema = Schema(implementation = Response::class))]
    )
    fun searchCommissionRate(
        @RequestBody
        searchCriteria: SearchCriteria
    ): Response {

        val clientLocation = ipStackService.execute(searchCriteria.client.ip)
        val freelancerLocation = ipStackService.execute(searchCriteria.freelancer.ip)

        return clientLocation.takeIf { it == freelancerLocation }
            ?.let { searchCommissionRate.execute(searchCriteria, it) }
            ?: throw IllegalArgumentException("Client and freelancer are not in the same country")
    }

    @PostMapping("/add")
    @Operation(
        summary = "Add a new Commission rate",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "The created Commission rate",
                content = [Content(
                    schema = Schema(implementation = CommissionRate::class),
                    examples = [ExampleObject(
                        name = "CommissionRateExample",
                        value = "{\"name\": \"Standard\", \"rate\": 10, \"restrictions\": {\"or\": [{\"mission_duration\": {\"\$gt\": \"2months\"}}, {\"commercial_relationship_duration\": {\"\$gt\": \"2months\"}}], \"country\": \"ES\"}}"
                    )]
                )]
            )
        ]
    )
    fun addCommissionRate(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [Content(
                schema = Schema(implementation = CommissionRate::class),
                examples = [ExampleObject(
                    name = "CommissionRateBodyExample",
                    value = "{\"name\": \"Standard\", \"rate\": 10, \"restrictions\": {\"or\": [{\"mission_duration\": {\"\$gt\": \"2months\"}}, {\"commercial_relationship_duration\": {\"\$gt\": \"2months\"}}], \"country\": \"ES\"}}"
                )]
            )]
        )
        @RequestBody
        commissionRate: CommissionRate
    ): CommissionRate {
        return addCommissionRateService.execute(commissionRate)
    }
}
