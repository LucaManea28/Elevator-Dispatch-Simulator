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

## Example: Execution

### External Call Command

```bash
curl -X POST "http://localhost:8080/api/call" \
  -H "Content-Type: application/json" \
  -d "{\"floor\":7,\"dir\":\"UP\",\"scheduler\":\"simple\"}"
  
  
### Initial Status

## After External Call – Status

```json
[
  {
    "id": 1,
    "currentFloor": 7,
    "state": "IDLE",
    "direction": "UP"
  },
  {
    "id": 2,
    "currentFloor": 0,
    "state": "IDLE",
    "direction": "UP"
  },
  {
    "id": 3,
    "currentFloor": 0,
    "state": "IDLE",
    "direction": "UP"
  }
]
```

## After External Call – Metrics

```json
{
  "totalRequests": 1,
  "servicedRequests": 1,
  "totalWaitMs": 3255,
  "averageWaitMs": 3255.0
}
```


## Scheduler Comparison (Simple vs Queue)

Benchmark configuration: `rate=3 req/s`, `duration=10s`, `floors=13`, `elevators=3`.

| Scheduler| Total Requests | Serviced Requests| Avg Wait (ms)  |
|----------|----------------|------------------|----------------|
| Simple   | 30             | 15               | 3639.2         |
| Queue    | 30             | 15               | 2801.06        |

Notes:
- Both tests were executed with the same load parameters.
- Metrics were collected after the test finished.

## Tech Stack

- Java 17+
- Spring Boot
- Maven
- REST API
- Multithreading
- JSON

## Run Locally

Requirements:
- Java 17+
- Maven

## Start the application:

- mvn spring-boot:run
- API base URL: http://localhost:8080