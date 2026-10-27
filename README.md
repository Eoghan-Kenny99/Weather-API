# Sensor Measurement API

## Overview

A REST API built with Java and Spring Boot for storing and querying sensor measurements. The API persists measurement data in PostgreSQL and supports statistical calculations across sensors, metrics, and time ranges.

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Features

- Store sensor measurements with a sensor ID, metric, value, and timestamp.
- Query measurements for one, multiple, or all sensors.
- Query one or multiple metrics.
- Retrieve the latest measurement when no date range is specified.
- Calculate `MIN`, `MAX`, `SUM`, and `AVERAGE` over a specified date range.
- Validate date ranges and return meaningful error messages.

## Prerequisites

- Java 17 or compatible JDK
- PostgreSQL
- A terminal such as PowerShell

## Configuration

1. Create a PostgreSQL database named `weatherdb`.
2. Open `src/main/resources/application.properties`.
3. Configure the database connection using your local PostgreSQL username, password, and database URL.


## Running the Application

From the project root directory, run:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

`http://localhost:8080`

## API Endpoints

### 1. Create a measurement

**POST** `/api/measurements`

Example request body:

```json
{
  "sensorId": "sensor-1",
  "metric": "TEMPERATURE",
  "value": 25.5,
  "time": "2026-10-22T11:30:00"
}
```

### 2. Query statistics for a sensor

**GET** `/api/measurements/statistics`

Example:

```text
/api/measurements/statistics?sensorId=sensor-1&metric=TEMPERATURE&statistic=AVERAGE&from=2026-10-22T00:00:00&to=2026-10-23T00:00:00
```

Supported statistics:

- `MIN`
- `MAX`
- `SUM`
- `AVERAGE`

When no date range is supplied, the endpoint returns the latest reading for the specified sensor and metric.

### 3. Query multiple sensors and metrics

**GET** `/api/measurements/query`

Example:

```text
/api/measurements/query?sensorId=sensor-1&sensorId=sensor-2&metric=TEMPERATURE&statistic=AVERAGE
```

To query all sensors, omit the `sensorId` parameter:

```text
/api/measurements/query?metric=TEMPERATURE&statistic=AVERAGE
```

Multiple metrics can be supplied by repeating the `metric` parameter:

```text
/api/measurements/query?metric=TEMPERATURE&metric=HUMIDITY&statistic=AVERAGE
```

When a date range is provided, the API calculates the requested statistic over readings within that range. Without a date range, it returns the latest reading for each matching sensor and metric.

## Date Range Validation

When specifying `from` and `to`:

- Both parameters must be supplied.
- The end must be after the start.
- The range must be at least one day.
- The range must not exceed one month.

Timestamps use the ISO 8601 local date-time format, for example `2026-10-22T11:30:00`.

## Running Tests

Run the automated tests from the project root:

```powershell
.\mvnw.cmd clean test
```

## Error Handling

The API validates request parameters and returns an error message for invalid date ranges, unsupported statistics, and queries that do not find matching measurements.
