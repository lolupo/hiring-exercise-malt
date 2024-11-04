package com.malt.hiringexercise.domain.repository

import com.malt.hiringexercise.domain.model.CommissionRate
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CommissionRateRepository : MongoRepository<CommissionRate, String> {

    @Query("{ \$and: [ { \$or: [ { 'restrictions.\$or.mission_duration': { \$exists: true } }, { 'restrictions.\$or.commercial_relationship_duration': { \$exists: true } } ] }, { 'restrictions.country': ?0 } ] }")
    fun findByRestrictionsCountry(country: String): List<CommissionRate>

}
