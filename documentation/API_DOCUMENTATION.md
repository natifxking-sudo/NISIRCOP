# API Documentation

This document provides detailed documentation for the NISIRCOP-LE-ANALYTICS API. All endpoints are secured and require a valid JWT token unless otherwise specified.

## System Architecture

The NISIRCOP system is built on a **microservices architecture** with the following components:
- **Eureka Server** (Port 8761): Service registry and discovery
- **API Gateway** (Port 8080): Single entry point for all client requests
- **Auth Service** (Port 8081): JWT-based authentication
- **User Service** (Port 8082): User management
- **Incident Service** (Port 8083): Crime incident management
- **Geographic Service** (Port 8084): Geospatial operations with PostGIS
- **Analytics Service** (Port 8085): Crime analytics and reporting

## Authentication

Authentication is handled by the `auth-service` using JWT tokens with 24-hour expiration.

### Login
**Endpoint**: `POST /auth/login`
**Request Body**:
```json
{
  "username": "officer_jane",
  "password": "password"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "officer_jane",
    "role": "OFFICER"
  }
}
```

### Using the Token
Include the JWT token in the Authorization header for all subsequent requests:
```
Authorization: Bearer <your_jwt_token>
```

## Services

This documentation is organized by microservice.

### 1. User Service (`/api/v1/users`)

- **GET /api/v1/users**: Get a list of all users. (Requires `SUPER_USER` role)
- **POST /api/v1/users**: Create a new user. (Requires `SUPER_USER` or `POLICE_STATION` role)
- **GET /api/v1/users/{id}**: Get details for a specific user.
- **GET /api/v1/users/username/{username}**: Get user details by username. (Internal endpoint for `auth-service`)

### 2. Incident Service (`/api/v1/incidents`)

- **POST /api/v1/incidents**: Report a new incident.
- **GET /api/v1/incidents**: Get a list of incidents.
- **GET /api/v1/incidents/{id}**: Get details for a specific incident.

### 3. Geographic Service (`/api/v1/geo`)

- **POST /api/v1/geo/validate**: Validate if a point is within a boundary.

### 4. Analytics Service (`/api/v1/analytics`)

- **GET /api/v1/analytics/trends**: Get crime trends over time.
- **GET /api/v1/analytics/heatmap**: Get data for a crime heat map.
