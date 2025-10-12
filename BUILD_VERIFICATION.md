# Build Verification Guide

## Java Version: 17 LTS (Locked)

All backend services are configured to use **Java 17 LTS** exclusively.

### Configuration Locations

#### 1. Gradle Build Files (`build.gradle`)
```gradle
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```
✅ Configured in all 7 backend services

#### 2. Docker Build Stage
```dockerfile
FROM gradle:8.5-jdk17-alpine AS build
```
✅ All services use JDK 17 for compilation

#### 3. Docker Runtime Stage
```dockerfile
FROM eclipse-temurin:17-jre-alpine
```
✅ All services use JRE 17 for execution

### Building the Application

#### Local Build (Requires Java 17)
If building locally, ensure Java 17 is installed:
```bash
java -version  # Should show Java 17
cd backend/eureka-server
./gradlew clean build
```

#### Docker Build (Recommended)
Docker builds are self-contained and include Java 17:
```bash
# Build single service
docker build -t eureka-server ./backend/eureka-server

# Build all services
docker-compose build
```

### Why Java 17?

1. **LTS Release**: Long-term support until September 2029
2. **Spring Boot 3.x Requirement**: Minimum Java 17
3. **Production Ready**: Stable, secure, and widely supported
4. **Modern Features**: Records, pattern matching, sealed classes

### Verification Commands

```bash
# Check Gradle configuration
grep -r "languageVersion = JavaLanguageVersion.of(17)" backend/*/build.gradle

# Check Docker build images
grep "jdk17-alpine" backend/*/Dockerfile

# Check Docker runtime images  
grep "17-jre-alpine" backend/*/Dockerfile
```

All should return 7 matches (one per service).
