# NISIRCOP-LE-ANALYTICS - Project README

## 📌 Project Overview

A comprehensive microservices-based police crime analytics platform built with:
- **Backend:** Java 17, Spring Boot 3.5.6, Spring Cloud, PostgreSQL + PostGIS
- **Frontend:** Vue.js 3, Vite, Tailwind CSS, Pinia
- **Infrastructure:** Docker, Docker Compose, Eureka, API Gateway

## 🎯 Current Status

**✅ Completed:** User Service (100% - 16 files, 17 tests, all passing)  
**🚧 In Progress:** Auth Service, Incident Service, Geographic Service, Analytics Service  
**📋 Planned:** Frontend components

## 📁 Project Structure

```
nisircop-le-analytics/
├── backend/
│   ├── user-service/        ✅ COMPLETE (16 Java files + tests)
│   ├── auth-service/        🚧 IN PROGRESS
│   ├── incident-service/    📋 PLANNED
│   ├── geographic-service/  📋 PLANNED
│   ├── analytics-service/   📋 PLANNED
│   ├── api-gateway/         ✅ CONFIGURED
│   └── eureka-server/       ✅ CONFIGURED
├── frontend/
│   ├── shell/              📋 Scaffolded
│   ├── users/              📋 Scaffolded
│   ├── incidents/          📋 Scaffolded
│   └── analytics/          📋 Scaffolded
├── database/
│   └── init.sql            ✅ Complete schema with PostGIS
└── docker-compose.yml      ✅ All services configured

```

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development)
- Node.js 16+ (for frontend)

### Environment Setup
```bash
# Create .env file
cat > .env << EOF
POSTGRES_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key_256_bits_min
