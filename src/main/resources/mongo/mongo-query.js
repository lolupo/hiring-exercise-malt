db.commission_rate.find({
  $and: [
    {
      $or: [
        { "restrictions.$or.mission_duration": { $exists: true } },
        { "restrictions.$or.commercial_relationship_duration": { $exists: true } }
      ]
    },
    { "restrictions.country": "ES" }
  ]
})