# README for API Endpoints

## Search Commission Rate API

**Endpoint**: `/v1/commission-rate/rate`

- **HTTP Method**: POST
- **Description**: This endpoint is used to fetch commission rates based on detailed search criteria, including mission length, duration of commercial relationship, and location data.

### Request Body Example

```json
{
  "client": {
    "ip": "192.168.1.1"
  },
  "freelancer": {
    "ip": "192.168.1.2"
  },
  "mission": {
    "length": "4months"
  },
  "commercialRelationship": {
    "firstMission": "2022-01-01T00:00:00",
    "lastMission": "2022-05-01T00:00:00"
  }
}
```

### Steps Performed by the Service:

1. **Extract Mission Length:**
- The service extracts a numeric value representing the mission length from a string input (e.g., "4months" would extract `4`).

2. **Calculate Commercial Relationship Duration:**
- It computes the duration in months between the first mission date and the last mission date.

3. **Fetch Commission Rates:**
- Retrieves applicable commission rates from the repository based on the common location (country) of the missions.

4. **Filter Commission Rates:**
- Filters these rates according to the restrictions defined in the search criteria provided.

5. **Return Commission Rate:**
- Returns the first commission rate that matches the criteria or throws an exception if no suitable rate is found.


## Add Commission Rate API

**Endpoint**: `/v1/commission-rate/add`

- **HTTP Method**: POST
- **Description**: This endpoint is used to add a new commission rate.

### Request Body Example

```json
{
   "name": "Standard",
   "rate": 10,
   "restrictions": {
      "or": [
         { "mission_duration": { "$gt": "2months" } },
         { "commercial_relationship_duration": { "$gt": "2months" } }
      ],
      "country": "ES"
   }
}
```

## Prerequisites

Before running the service, ensure you have:

- **Docker** installed on your machine.

### Steps to Prepare:

1. **Build the Docker Image:**
   ```bash
   cd src/main/resources
   docker build -f Mongo.dockerfile -t mongo .
   docker run -d -p 27017:27017 --name my-mongo-base mongo