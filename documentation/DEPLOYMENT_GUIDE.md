# Deployment Guide

This guide provides comprehensive instructions for deploying the NISIRCOP-LE-ANALYTICS platform using Docker.

## 1. Prerequisites

- **Docker Desktop** (includes Docker and Docker Compose)
  - Windows/Mac: Download from https://www.docker.com/products/docker-desktop
  - Linux: Install Docker Engine and Docker Compose separately
- **Git** (for cloning the repository)
- **Minimum System Requirements**:
  - 8GB RAM
  - 20GB free disk space
  - Multi-core processor

## 2. Quick Start

### 2.1 Clone the Repository
```bash
git clone <repository-url>
cd nisircop
```

### 2.2 Setup Environment Variables

A `.env.example` file is provided in the root directory. Copy it to create your environment file:

```bash
cp .env.example .env
```

Edit the `.env` file with your own secure values:

```env
# Database Configuration
POSTGRES_PASSWORD=your_strong_database_password_here

# JWT Configuration (for auth-service)
# Generate a secure secret key (e.g., using: openssl rand -base64 64)
JWT_SECRET=your_jwt_secret_key_here
```

**Security Recommendations**:
- `POSTGRES_PASSWORD`: Use a strong password (minimum 16 characters, mix of letters, numbers, symbols)
- `JWT_SECRET`: Use a cryptographically secure random string (at least 256 bits / 32 bytes)
  - Generate on Linux/Mac: `openssl rand -base64 64`
  - Generate on Windows PowerShell: `[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))`

**⚠️ IMPORTANT**: Never commit the `.env` file to version control! It's already included in `.gitignore`.

## 3. Running the Application

### 3.1 Build and Start All Services

From the project root directory, run:

```bash
docker-compose up --build -d
```

This command will:
1. **Build** Docker images for all services (7 backend + 1 frontend)
2. **Download** required base images (PostgreSQL, Nginx, etc.)
3. **Create** necessary networks and volumes
4. **Start** all containers in detached mode (background)

**Note**: First build may take 10-15 minutes depending on your internet connection and machine specs.

### 3.2 Monitor Service Startup

Watch the logs to ensure services start correctly:

```bash
docker-compose logs -f
```

**Startup Order** (handled automatically by health checks):
1. **PostgreSQL Database** (5-10 seconds)
2. **Eureka Server** (20-30 seconds)
3. **All Microservices** (30-45 seconds each)
4. **Frontend Application** (5-10 seconds)

Press `Ctrl+C` to exit log viewing (containers keep running).

### 3.3 Verify All Services Are Running

```bash
docker-compose ps
```

All services should show status as "Up" and "healthy" (for services with healthchecks).

## 4. Accessing the Services

Once all services are running, access them at:

### Primary Services
- **Frontend Application**: http://localhost:3000
  - Main user interface for the NISIRCOP system
  - Login with demo credentials (see section 4.1)

- **API Gateway**: http://localhost:8080
  - Single entry point for all backend APIs
  - Routes requests to appropriate microservices

- **Eureka Discovery Server**: http://localhost:8761
  - Service registry dashboard
  - Shows all registered microservices and their health status

### Individual Microservices (for development/debugging)
- **Auth Service**: http://localhost:8081
- **User Service**: http://localhost:8082
- **Incident Service**: http://localhost:8083
- **Geographic Service**: http://localhost:8084
- **Analytics Service**: http://localhost:8085

**Note**: In production, clients should only access the API Gateway (8080) and Frontend (3000).

### 4.1 Demo Credentials

The system is pre-populated with demo users (password for all is `password`):

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| super_user | password | SUPER_USER | Full system access |
| station_one | password | POLICE_STATION | Station administrator |
| officer_jane | password | OFFICER | Police officer |
| officer_john | password | OFFICER | Police officer |

## 5. Managing the Application

### 5.1 Stopping the Application

**Stop all containers** (preserves data):
```bash
docker-compose stop
```

**Stop and remove containers** (preserves data in volumes):
```bash
docker-compose down
```

**Stop and remove everything including data**:
```bash
docker-compose down -v
```
⚠️ **Warning**: This deletes all database data!

### 5.2 Restarting Services

**Restart all services**:
```bash
docker-compose restart
```

**Restart a specific service**:
```bash
docker-compose restart user-service
```

### 5.3 Viewing Logs

**All services**:
```bash
docker-compose logs
```

**Specific service**:
```bash
docker-compose logs user-service
```

**Follow logs (live stream)**:
```bash
docker-compose logs -f user-service
```

**Last N lines**:
```bash
docker-compose logs --tail=100 user-service
```

### 5.4 Rebuilding After Code Changes

After modifying code, rebuild and restart:
```bash
docker-compose up --build -d
```

Or rebuild a specific service:
```bash
docker-compose up --build -d user-service
```

## 6. Troubleshooting

### 6.1 Port Already in Use

**Error**: `Bind for 0.0.0.0:8080 failed: port is already allocated`

**Solution**: Another application is using the port. Either:
- Stop the conflicting application
- Change the port in `docker-compose.yml`:
  ```yaml
  ports:
    - "8081:8080"  # Use port 8081 on host instead
  ```

### 6.2 Services Won't Start

**Check service status**:
```bash
docker-compose ps
```

**Check logs for errors**:
```bash
docker-compose logs service-name
```

**Common issues**:
- Missing `.env` file → Create from `.env.example`
- Database not ready → Wait for healthcheck to pass
- Build errors → Check Java/Node versions in Dockerfile
- Out of memory → Increase Docker memory limit

### 6.3 Database Connection Errors

**Verify database is running**:
```bash
docker-compose ps db
```

**Check database logs**:
```bash
docker-compose logs db
```

**Test database connection**:
```bash
docker-compose exec db psql -U postgres -d nisircop
```

### 6.4 Complete Reset

If everything breaks, perform a complete reset:

```bash
# Stop and remove everything
docker-compose down -v --rmi all

# Remove any orphaned containers
docker container prune -f

# Rebuild from scratch
docker-compose up --build
```

## 7. System Requirements

### 7.1 Technology Stack

| Component | Version | Status |
|-----------|---------|--------|
| Java | 17 LTS | ✅ Locked |
| Spring Boot | 3.5.6 | ✅ Latest |
| Spring Cloud | 2024.0.0 | ✅ Latest |
| PostgreSQL | 15 + PostGIS 3.3 | ✅ Stable |
| Node.js | 20 | ✅ Latest LTS |
| Vue.js | 3.5.22 | ✅ Latest |
| Docker | 20+ | ✅ Required |

### 7.2 Architecture Overview

```
┌─────────────────────────────────────────────────┐
│              Eureka Server (8761)               │
│           Service Registry & Discovery          │
└──────────────────┬──────────────────────────────┘
                   │
      ┌────────────┼────────────┐
      │                         │
┌─────▼──────┐          ┌──────▼──────┐
│ API Gateway│          │  PostgreSQL │
│   (8080)   │          │  + PostGIS  │
└─────┬──────┘          └──────┬──────┘
      │                        │
      │    ┌───────────────────┼─────────────┐
      │    │                   │             │
┌─────▼────▼───┐  ┌───────────▼────┐  ┌─────▼────────┐
│ Auth Service │  │  User Service  │  │   Incident   │
│    (8081)    │  │    (8082)      │  │   Service    │
└──────────────┘  └────────────────┘  │   (8083)     │
                                      └──────────────┘
┌──────────────┐  ┌────────────────┐
│ Geographic   │  │   Analytics    │
│   Service    │  │    Service     │
│   (8084)     │  │    (8085)      │
└──────────────┘  └────────────────┘

┌──────────────────────────────────┐
│     Frontend Application         │
│          (Port 3000)             │
└──────────────────────────────────┘
```

## 8. Production Deployment Considerations

### 8.1 Security

✅ **Implemented**:
- JWT authentication with 24-hour expiration
- BCrypt password hashing
- Environment variable configuration
- Database schema validation (no auto-modification)

⚠️ **Additional recommendations**:
- Use HTTPS/TLS in production
- Implement rate limiting
- Enable Spring Security CSRF protection
- Use secrets management (e.g., AWS Secrets Manager, HashiCorp Vault)

### 8.2 Scalability

The microservices architecture allows independent scaling:

```bash
# Scale user service to 3 instances
docker-compose up --scale user-service=3 -d
```

API Gateway automatically load-balances across instances via Eureka.

### 8.3 Monitoring

**Health Checks**: All services include Docker healthchecks

**Verify health**:
```bash
docker-compose ps
```

**Database monitoring**:
```bash
docker-compose exec db psql -U postgres -d nisircop -c "SELECT count(*) FROM incidents;"
```

## 9. Support

For issues or questions:
- Check logs: `docker-compose logs <service-name>`
- Review documentation in `/documentation` folder
- Check `FIXES_APPLIED.md` for known issues and resolutions
