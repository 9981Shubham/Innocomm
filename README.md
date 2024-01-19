# InnoComm

## Project Description

InnoComm is a platform developed by InnoComm Solutions for robust, user-friendly, and scalable SMS communication. It caters to both individual and enterprise-level clients. The project utilizes Java, Spring Boot, Kafka, Swagger, Spring Security, MySQL database, Prometheus, Grafana, and Log4j. Notably, it doesn't send SMS itself but relies on a mocked external provider.

## Technologies Used

- Java
- Spring Boot
- Kafka
- Swagger
- Spring Security(JWT)
- H2 Database
- Log4j
- Actuator

## Prerequisites

Ensure the following software/tools are installed before running the project:

- Kafka
- Java version 17
- Spring 2.7

## Installation Instructions

1. Clone the project to your local workspace using Git or another method.
2. Run Zookeeper server then Kafka server.properties.
3. Run the application as a Java application.
4. Access the API documentation at [http://localhost:8092/swagger-ui.html#!](http://localhost:8092/swagger-ui.html#!).
5. health at http://localhost:7878/actuator/
## Configuration

- Set JAVA_HOME to the bin folder as an environment variable.
- Configure the log file destination for Kafka.

## Usage

API usage details are provided in the [API documentation](http://localhost:8092/swagger-ui.html#!).

## Examples

### Send Single SMS

**API Endpoint**: http://localhost:8092/smsSender/sendSingleSMS

**Request:**
```json
{
  "message": "Callback functionality implementations.",
  "phoneNumber": "+919981763130",
  "userId": "1"
}

**Response:**
```json
{
  "message": "DELIVERED",
  "status": "SUCCESS",
  "response": [
    {
      "message": "Callback functionality implementations.",
      "phoneNumber": "+919981763130",
      "userId": "1"
    }
  ],
  "statusCode": "200"
}

## API Documentation Link
Access the detailed API documentation at [API documentation](http://localhost:8092/swagger-ui.html#!).

## Contributors
-Shubham Raikhere

## License
-All source code is openly available.

## Database design
-H2-database implemented.for ER refer:InnoComm/erDiagram.jpg

##Monitoring
-Actuators endpoint(http://localhost:7878/actuator/)