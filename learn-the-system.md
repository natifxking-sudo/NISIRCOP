# Learning the NISIRCOP-LE-ANALYTICS Platform

## Module 1: Introduction to the System

### 1.1. What is NISIRCOP-LE-ANALYTICS?
NISIRCOP-LE-ANALYTICS is a modern, microservices-based crime analytics platform designed for law enforcement agencies. It replaces traditional paper-based systems with a real-time, data-driven approach to crime reporting and analysis.

### 1.2. The Vision
The goal is to provide a scalable, secure, and user-friendly platform that empowers law enforcement with the tools they need to make informed decisions, identify crime patterns, and allocate resources effectively.

### 1.3. Core Concepts
- **Microservices:** The system is broken down into small, independent services that communicate with each other. This makes the system easier to develop, deploy, and scale.
- **Microfrontends:** The user interface is also broken down into smaller, independent parts, allowing for parallel development and deployment of new features.
- **Geospatial Data:** The system heavily relies on geographic data to track incident locations, define operational boundaries, and perform spatial analysis.
- **Role-Based Access Control (RBAC):** Access to features and data is strictly controlled based on the user's role (e.g., OFFICER, POLICE_STATION, SUPER_USER).

## Module 2: The Technology Stack

### 2.1. Backend: Java and Spring Boot
- **Java 17:** We use a modern version of Java for its performance, security, and long-term support.
- **Spring Boot 3:** This framework makes it easy to create stand-alone, production-grade Spring-based applications. It simplifies the setup and configuration of our microservices.
- **Spring Cloud:** This provides tools for building distributed systems, including service discovery (Eureka) and an API gateway.

### 2.2. Frontend: Vue.js
- **Vue 3:** A progressive JavaScript framework for building user interfaces. We use it for its simplicity, performance, and rich ecosystem.
- **Vite:** A modern frontend build tool that provides an extremely fast development experience.
- **Pinia:** The official state management library for Vue.js. We use it to manage the application's state, such as the logged-in user.
- **Tailwind CSS:** A utility-first CSS framework that allows us to rapidly build custom user interfaces.

### 2.3. Database: PostgreSQL with PostGIS
- **PostgreSQL 15:** A powerful, open-source object-relational database system known for its reliability and robustness.
- **PostGIS 3.3:** An extension for PostgreSQL that adds support for geographic objects, allowing us to perform powerful spatial queries.

### 2.4. Containerization: Docker
- **Docker:** A platform for developing, shipping, and running applications in containers. We use Docker to ensure that our development environment is identical to our production environment.
- **Docker Compose:** A tool for defining and running multi-container Docker applications. Our `docker-compose.yml` file defines all the services that make up our application.

## Module 3: Architecture Deep Dive

### 3.1. The Microservices
- **API Gateway:** The single entry point for all client requests. It handles routing, security, and other cross-cutting concerns.
- **Auth Service:** Responsible for user authentication and JWT token generation.
- **User Service:** Manages user data, profiles, and roles.
- **Geographic Service:** Handles all geospatial operations, such as boundary validation.
- **Incident Service:** Manages crime incident data.
- **Analytics Service:** Provides data for crime trend analysis and reporting.

### 3.2. The Microfrontends
- **Shell:** The main application container. It's responsible for the overall layout, navigation, and loading the other microfrontends.
- **Incidents Module:** Allows users to report and view incidents.
- **Users Module:** Provides tools for user management.
- **Analytics Module:** Displays charts, maps, and reports.

### 3.3. How They Communicate
- The frontend applications communicate with the backend via the **API Gateway**.
- The backend microservices communicate with each other using a combination of direct REST calls (via Feign clients) and, in a future iteration, asynchronous messaging.
- All backend services register with the **Eureka Discovery Server**, which allows them to find and communicate with each other without hardcoding URLs.

## Module 4: Getting Involved

### 4.1. Setting up your Environment
1. Clone the repository.
2. Make sure you have Docker and Docker Compose installed.
3. Create a `.env` file (as described in the `DEPLOYMENT_GUIDE.md`).
4. Run `docker-compose up --build -d`.

### 4.2. The Codebase
- The `backend` directory contains the source code for all the Spring Boot microservices.
- The `frontend` directory contains the source code for the Vue.js microfrontends.
- The `database` directory contains the SQL scripts for initializing the database.
- The `documentation` directory contains all the project documentation (including this file!).

### 4.3. Our Development Philosophy
- **Clean Code:** We strive to write code that is easy to read, understand, and maintain.
- **Test-Driven Development (TDD):** We write tests before we write code to ensure that our code is correct and to prevent regressions.
- **Continuous Integration/Continuous Deployment (CI/CD):** We use automated pipelines to build, test, and deploy our code.

---
We're excited to have you on board! If you have any questions, please don't hesitate to ask.