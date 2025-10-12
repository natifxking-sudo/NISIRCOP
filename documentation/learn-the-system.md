# NISIRCOP-LE-ANALYTICS: Technical Overview

## 1. Introduction

### 1.1. What is NISIRCOP-LE-ANALYTICS?
NISIRCOP-LE-ANALYTICS is a modern, microservices-based crime analytics platform for law enforcement agencies. It provides a real-time, data-driven approach to crime reporting and analysis, replacing traditional paper-based systems.

### 1.2. The Vision
The platform is designed to be a scalable, secure, and user-friendly tool that empowers law enforcement to make informed decisions, identify crime patterns, and allocate resources effectively.

### 1.3. Core Concepts
- **Microservices:** The system is composed of small, independent services that communicate with each other, making it easier to develop, deploy, and scale.
- **Microfrontends:** The user interface is broken down into smaller, independent parts, allowing for parallel development and deployment.
- **Geospatial Data:** The platform heavily relies on geographic data to track incident locations, define operational boundaries, and perform spatial analysis.
- **Role-Based Access Control (RBAC):** Access to features and data is strictly controlled based on user roles (OFFICER, POLICE_STATION, SUPER_USER).

## 2. Technology Stack

### 2.1. Backend
- **Java 17:** A modern version of Java for performance, security, and long-term support.
- **Spring Boot 3:** A framework for creating stand-alone, production-grade Spring-based applications.
- **Spring Cloud:** A set of tools for building distributed systems, including service discovery (Eureka) and an API gateway.

### 2.2. Frontend
- **Vue 3:** A progressive JavaScript framework for building user interfaces.
- **Vite:** A modern frontend build tool for a fast development experience.
- **Pinia:** The official state management library for Vue.js.
- **Tailwind CSS:** A utility-first CSS framework for rapid UI development.

### 2.3. Database
- **PostgreSQL 15:** A powerful, open-source object-relational database system.
- **PostGIS 3.3:** An extension for PostgreSQL that adds support for geographic objects.

### 2.4. Containerization
- **Docker:** A platform for developing, shipping, and running applications in containers.
- **Docker Compose:** A tool for defining and running multi-container Docker applications.

## 3. Architecture

### 3.1. Backend Microservices
- **Eureka Server (`eureka-server`):** A service discovery server that allows services to find and communicate with each other. (Port: 8761)
- **API Gateway (`api-gateway`):** The single entry point for all client requests, handling routing, security, and other cross-cutting concerns. (Port: 8080)
- **Auth Service (`auth-service`):** Responsible for user authentication and JWT token generation. (Port: 8081)
- **User Service (`user-service`):** Manages user data, profiles, and roles. (Port: 8082)
- **Incident Service (`incident-service`):** Manages crime incident data. (Port: 8083)
- **Geographic Service (`geographic-service`):** Handles all geospatial operations, such as boundary validation. (Port: 8084)
- **Analytics Service (`analytics-service`):** Provides data for crime trend analysis and reporting. (Port: 8085)

### 3.2. Frontend Microfrontends
- **Shell (`shell`):** The main application container, responsible for the overall layout and navigation. (Served at `/`)
- **Incidents Module (`incidents`):** Allows users to report and view incidents. (Served at `/incidents`)
- **Users Module (`users`):** Provides tools for user management. (Served at `/users`)
- **Analytics Module (`analytics`):** Displays charts, maps, and reports. (Served at `/analytics`)

All frontend applications are built as static assets and served by a single Nginx container.

### 3.3. Communication
- **Frontend to Backend:** The frontend applications communicate with the backend via the **API Gateway**.
- **Backend to Backend:** The backend microservices communicate with each other using REST calls. They discover each other using the **Eureka Discovery Server**.
- **Backend to Database:** All backend services that require data persistence communicate with the **PostgreSQL** database.

## 4. Getting Started

### 4.1. Prerequisites
- Docker
- Docker Compose

### 4.2. Running the Application
1.  Clone the repository.
2.  Create a `.env` file in the root of the project with the required environment variables (see `DEPLOYMENT_GUIDE.md`).
3.  Run `docker-compose up --build -d` from the project root.

### 4.3. Codebase Structure
- `backend/`: Contains the source code for all the Spring Boot microservices.
- `frontend/`: Contains the source code for the Vue.js microfrontends.
- `database/`: Contains the SQL scripts for initializing the database.
- `documentation/`: Contains all the project documentation.
- `docker-compose.yml`: Defines all the services that make up the application.
