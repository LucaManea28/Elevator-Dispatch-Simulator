# Elevator Dispatch Simulation

A multi-elevator simulation system built with Java and Spring Boot, featuring configurable dispatch algorithms, multithreaded execution, real-time performance metrics, and a REST API for interactive control and benchmarking.

## Features

- Distance-based (Simple) scheduling algorithm
- Queue-aware scheduling algorithm (load-based penalty)
- Multithreaded elevator simulation (background tick loop)
- Stochastic traffic generator for load testing
- Real-time performance metrics:
    - Total requests
    - Serviced requests
    - Average wait time
- REST API for:
    - External elevator calls
    - Internal elevator commands
    - Automated benchmarking
    - System reset and monitoring

## Architecture

The project is structured with clear separation of concerns:

domain/ → Elevator state machine and building logic  
metrics/ → Performance statistics tracking  
simulation/ → Traffic generation engine  
Scheduler/ → Dispatch algorithm implementations  
web/ → REST controllers, services, DTOs

The simulator runs continuously in a background thread while REST requests safely interact with the system.

## REST API Endpoints

GET /api/status  
GET /api/metrics  
POST /api/call  
POST /api/go  
POST /api/test  
POST /api/reset

### Example: External Call

```json
{
  "floor": 7,
  "dir": "UP",
  "scheduler": "simple"
}
```

### Example: Internal Command

```json
{
"elevatorId": 1,
"targetFloor": 10
}
```

### Example: Benchmark Test

```json
{
"scheduler": "queue",
"rate": 3,
"seconds": 10
}
```

## Tech Stack

Java 17+
Spring Boot
Maven
REST API
Multithreading
JSON

## Run Locally

Requirements:
- Java 17+
- Maven

## Start the application:

- mvn spring-boot:run
- API base URL: http://localhost:8080