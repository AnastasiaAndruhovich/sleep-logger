# Sleep Logger API - Backend Assignment

Welcome to the **Sleep Logger API** project! This repository contains a fully implemented backend system for logging and analyzing sleep data. The project demonstrates proficiency in RESTful API design, database modeling, and test-driven development, and is built as part of a backend engineering take-home assignment.

---

## Features

The Sleep Logger API supports the following functionality:

### 1. **Log Last Night’s Sleep**
- **Endpoint**: Create a new sleep log.
- **Data Requirements**:
    - Date of sleep (e.g., today’s date).
    - Time spent in bed (start and end times).
    - Total time in bed (calculated).
    - Morning feeling (`BAD`, `OK`, or `GOOD`).

### 2. **Fetch Last Night’s Sleep**
- Retrieve details of the most recent sleep log.

### 3. **Get 30-Day Averages**
- **Data Provided**:
    - Date range for averages.
    - Average total time in bed.
    - Average bedtime and wake-up time.
    - Morning feeling frequencies.

### 4. **Fetch 30-Day Sleep Logs**
- Retrieve details of 30-Days sleep log.

### 5. **Fetch All Available Users**
- Retrieve All Available Users with Generated Mocked tokens, so you could use it to play around.

---

## Getting Started

### Prerequisites

Ensure the following are installed on your system:
- **Docker**: For containerized setup.
- **Postman** (or any API testing tool): To test endpoints. It's optional. You can use swagger instead.
- Open ports **5432** (PostgreSQL) and **8080** (API).

---

### Running the Application

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd <repository-directory>

2. **Run Application**:
   ```bash
   docker-compose up --build

3. **Access the API**:
   - The API will be available at http://localhost:8080/api/sleep-logger
   - Swagger will be available at http://localhost:8080/api/sleep-logger/swagger-ui/index.html

---

## Database Schema

Link to the designed DB schema is https://drawsql.app/teams/nastya-andr/diagrams/sleep.


## Testing

### Unit Tests

**Coverage for**:
  - Business logic validations
  - Mapper tests for DTO-to-Entity conversions

### Integration Tests

**Coverage for**:
  - Repository methods

### How to Run Tests
1. Ensure Docker containers are running (docker-compose up).
2. Use your preferred IDE or run the tests via:
   ```bash
   ./gradlew test

---

## Development Tools and Frameworks
- Language: Java 11 
- Framework: Spring Boot 2.7.17
- Database: PostgreSQL 
- Build Tool: Gradle 
- ORM: Spring Data JPA - Hibernate
- Testing: JUnit 5

### Development process tracking
It can be tracked within github project https://github.com/users/AnastasiaAndruhovich/projects/3

---

## Future Enhancements
- Migrate from H2 to Postgres in integration tests in order to have an opportunity to test Postgres dialect specific queries
- Cache last night results
- Add authentication and user roles