# API Documentation

This document provides detailed documentation for the NISIRCOP-LE-ANALYTICS API. All endpoints are secured and require a valid JWT token unless otherwise specified.

## Authentication

Authentication is handled by the `auth-service`. To obtain a token, send a POST request to `/auth/login` with user credentials.

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

---
*This is a placeholder document. It will be automatically updated from OpenAPI/Swagger definitions during the build process.*