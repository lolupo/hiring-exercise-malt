package com.malt.hiringexercise.api

import com.malt.hiringexercise.api.dto.Response
import com.malt.hiringexercise.api.dto.SearchCriteria
import com.malt.hiringexercise.domain.model.CommissionRate
import com.malt.hiringexercise.service.AddCommissionRateService
import com.malt.hiringexercise.service.SearchCommissionRate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/commission-rate")
class CommissionRateController(
    private val searchCommissionRate: SearchCommissionRate,
    private val addCommissionRateService: AddCommissionRateService
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

        return searchCommissionRate.execute(searchCriteria)
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
                        value = "{\"name\": \"Standard\", \"rate\": 10, \"restrictions\": \"{\\\"@and\\\": [{\\\"mission_duration\\\": {\\\"gt\\\": \\\"2months\\\"}}, {\\\"commercial_relationship_duration\\\": {\\\"gt\\\": \\\"2months\\\"}}], \\\"@client.location\\\": {\\\"country\\\": \\\"ES\\\"}, \\\"@freelancer.location\\\": {\\\\\"country\\\": \\\"ES\\\"}}\"}"
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
                    value = "{\"name\": \"Standard\", \"rate\": 10, \"restrictions\": \"{\\\"@and\\\": [{\\\"mission_duration\\\": {\\\"gt\\\": \\\"2months\\\"}}, {\\\"commercial_relationship_duration\\\": {\\\"gt\\\": \\\"2months\\\"}}], \\\"@client.location\\\": {\\\"country\\\": \\\"ES\\\"}, \\\"@freelancer.location\\\": {\\\"country\\\": \\\"ES\\\"}}\"}"
                )]
            )]
        )
        @RequestBody
        commissionRate: CommissionRate
    ): ResponseEntity<CommissionRate> {
        val createdCommissionRate = addCommissionRateService.execute(commissionRate)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommissionRate)
    }
}