

# IFCO Telemetry Challenge — Backend Service

A high-performance backend service for ingesting, processing, and querying device telemetry using **Spring Boot**, **PostgreSQL**, **Redis**, and **Kafka**.  
The project follows **Clean Architecture** and **CQRS** to ensure scalability and maintainability.

----------
<br/>

## Features

### **Telemetry Ingestion**

`POST /telemetry`  
Persists telemetry into PostgreSQL and updates a Projection in Redis.

### **Latest Telemetry Query**

`GET /telemetry/getLatest`  
Returns the latest telemetry for each device from Redis.

### **Duplicate Handling**

Custom domain logic (`TelemetryAnalyzer`) prevents duplicates and automatically cleans them if found.


### **Docker Secrets Support**

Credentials are loaded automatically from Docker secrets via an `EnvironmentPostProcessor`.

----------
<br/>


## Running in Docker

A preconfigured Dockerfile is included to create the image for the service.

There is also a compose.yaml with all the required services pre-configured:

compose.yaml <br/>
 ├── redis          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;  # Projection DB for queries<br/>
 ├── postgres       &emsp;&emsp;&emsp;&ensp;&ensp;&ensp;     	  # DB for commands<br/>
 ├── kafka          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;    	  # Messaging Service for Events<br/>
 ├── backend        &emsp;&emsp;&emsp;&emsp;&nbsp;&nbsp;        # The image of the service<br/>
 └── influxdb2		  &emsp;&emsp;&emsp;&emsp;&nbsp;         	    # (Optional) Influxdb to export metrics<br/>
    
 This compose is configured to use the following 4 secrets stored in a folder called "config": influx.pass, influx.token, postgres.pass, redis.pass
 

### Docker Secrets
 This service automatically loads secrets located at:
`/run/secrets/` 

Docker Compose is pre-configured to use the following 4 secrets stored in a folder called `config`: <br/>
-   `postgres.pass`
-   `redis.pass`
-   `influx.pass`
-   `influx.token`

----------
<br/>

## API Endpoints

### **POST /telemetry**
Record a new telemetry entry.

Request Body:
`{"deviceId":  1,  "measurement":  18.5,  "date":  "2025-01-01T12:00:00Z"}` 

Response: `202 Accepted`

<br/>

### **GET /telemetry/getLatest**
Retrieve the latest telemetry for all devices.

Sample response:
`[ {"deviceId":  1,  "temperature":  18.5,  "date":  "2025-01-01T12:00:00Z"}  ]` 

----------
<br/>

## Build & Run Locally
```
git clone https://github.com/Elysaveth/ifco-challenge
docker compose up -d
```


----------
<br/>

## Application Properties
| Property   |      Description      |
|----------|:-------------:|
| spring.datasource.* |  PostgreSQL config|
| spring.data.redis.* |    Redis config   |
| spring.kafka.bootstrap-servers | Kafka broker |
| topics.telemetry-recorded | Kafka topic name |
| spring.management.influx.* | Influx config |

<br/><br/>

## License
MIT License — see `LICENSE` file.
