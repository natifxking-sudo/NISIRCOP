# Deployment Guide

This guide provides instructions for deploying the NISIRCOP-LE-ANALYTICS platform using Docker.

## Prerequisites

- Docker
- Docker Compose
- A `.env` file with the required environment variables.

## Environment Variables

Create a `.env` file in the root of the project with the following variables:

```
POSTGRES_PASSWORD=your_strong_database_password
JWT_SECRET=your_super_secret_jwt_key_that_is_at_least_256_bits_long
```

- `POSTGRES_PASSWORD`: The password for the PostgreSQL database user.
- `JWT_SECRET`: A secure secret key for signing JWT tokens.

## Running the Application

To build and run the entire application stack, use the following command from the project root:

```bash
docker-compose up --build -d
```

This will:
1. Build the Docker images for all microservices and frontend applications.
2. Start all the containers in detached mode.
3. Set up the network and volumes.

## Accessing the Services

Once the application is running, the services will be available at the following URLs:

- **Frontend Application**: `http://localhost:3000`
- **API Gateway**: `http://localhost:8080`
- **Eureka Discovery Server**: `http://localhost:8761`

## Stopping the Application

To stop all the running containers, use the following command:

```bash
docker-compose down
```

To stop and remove the volumes (which will delete all data), use:

```bash
docker-compose down -v
```