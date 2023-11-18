# CNAT Tracker Service
The Tracker Service handles requests related to trackers and their data. This microservice both 
listens for REST API calls and events, and has the responsibility of validating requests before 
processing them.

## Software Architecture:
The software architecture in this microservice consists of three layers; Controller layer, service 
layer, and repository layer. Request objects are used to define the request body for each 
endpoint and responses are either a single or an array of DTOs

The controller layer is responsible for creating the URL mapping with the required method, 
response type, and body or query parameter, and also field validation. As the API Gateway has 
performed the required security checks on the requests, this microservice does not put any form 
of authentication in place.
In contrast with API Gateway the service layer in this application acts as the middleman between 
the controller layer and the repository layer. It calls the necessary repository methods to 
perform the request and parses the response into the appropriate object for the controller layer.
The repository layer in this architecture is an interface for performing database operations. The 
statements for these operations are defined and executed in this layer

## API:
| Method | Path         | Request                                      | Response         | Description                                      |
|--------|--------------|----------------------------------------------|------------------|--------------------------------------------------|
| POST   | /            | Body: TrackerRegisterRequest                 | TrackerDTO       | Register a new tracker                           |
| GET    | /            | Query param: userId                          | TrackerDTO[]     | Get all user’s trackers                          |
| GET    | /{id}        |                                              | TrackerDTO       | Get tracker                                      |
| DELETE | /{id}        |                                              | Status code      | Delete tracker                                   |
| DELETE | /            | Query param: userId                          | Status code      | Delete all user's tracker                        |
| GET    | /{id}/data   | Query param: from, to, hasCoordinates, limit | TrackerDataDTO[] | Get tracker’s data                               |
| GET    | /data/latest | Query param: userId                          | TrackerDataDTO[] | Get latest data form each of the user’s trackers |

## Deployment

Using Docker:
```bash
docker build -t cnat-tracker-service .
docker run --name some-cnat-tracker-service -dp 80:80 \
  -e CNAT_TRACKER_SERVICE_MONGODB_URI=your_mongodb_uri \
  -e CNAT_TRACKER_SERVICE_MONGODB_DATABASE=your_mongodb_database \
  -e CNAT_TRACKER_SERVICE_KAFKA_CONSUMER_GROUP_ID=your_kafka_consumer_group_id \
  -e CNAT_KAFKA_TRACKER_DATA_TOPIC=your_tracker_data_topic \
  cnat-tracker-service
```

Using Maven:
```bash
mvn clean package
java -jar \
  -DCNAT_TRACKER_SERVICE_MONGODB_URI=your_mongodb_uri \
  -DCNAT_TRACKER_SERVICE_MONGODB_DATABASE=your_mongodb_database \
  -DCNAT_TRACKER_SERVICE_KAFKA_CONSUMER_GROUP_ID=your_kafka_consumer_group_id \
  -DCNAT_KAFKA_TRACKER_DATA_TOPIC=your_tracker_data_topic \
  target/cnat-tracker-service-0.0.1-SNAPSHOT.jar
```
