# NISIRCOP-LE Backend - Complete Learning Course

Welcome to the complete learning course for the NISIRCOP-LE backend system! This course will take you from beginner to advanced, teaching you how to work with this microservices architecture.

## 🎯 Course Status

This course teaches you using the **fully implemented User Service** as a reference example. You'll learn by exploring real, working code that's been tested and verified.

**What's Available**:
- ✅ Complete User Service implementation to study
- ✅ Working database with sample data
- ✅ All infrastructure services operational
- ✅ Swagger UI for interactive API testing
- ✅ Maven build system
- ✅ Spring Boot microservices framework

---

## Table of Contents

- [Lesson 1: Introduction to NISIRCOP-LE Backend](#lesson-1-introduction-to-nisircop-le-backend)
- [Lesson 2: Setting Up Your Development Environment](#lesson-2-setting-up-your-development-environment)
- [Lesson 3: Understanding the Core Architecture](#lesson-3-understanding-the-core-architecture)
- [Lesson 4: How Requests Flow Through the System](#lesson-4-how-requests-flow-through-the-system)
- [Lesson 5: Working with the Database](#lesson-5-working-with-the-database)
- [Lesson 6: Adding New Features](#lesson-6-adding-new-features)
- [Lesson 7: Testing and Debugging](#lesson-7-testing-and-debugging)
- [Final Project](#final-project)

---

## Lesson 1: Introduction to NISIRCOP-LE Backend

### What You'll Learn
- What is NISIRCOP-LE and why microservices?
- Overview of the system architecture
- The role of each service

### 1.1 What is NISIRCOP-LE?

**NISIRCOP-LE** stands for **NISIR Common Operational Picture For Law Enforcement**. It's a system designed to help Ethiopian police forces:

- Report crimes digitally with GPS locations
- Analyze crime patterns and trends
- Manage police officers and stations hierarchically
- Perform geographic analysis (which crimes happened where?)
- Generate analytics and reports

### 1.2 Why Microservices?

Instead of one big application, NISIRCOP-LE is split into **7 independent services**:

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENT (Web/Mobile)                   │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌────────────────────────────────────────────────────────┐
│         API GATEWAY (Port 8080)                        │
│         - Single entry point                           │
│         - Routes requests to services                  │
└───────┬──────┬──────┬──────┬──────┬──────────────────┘
        │      │      │      │      │
        ↓      ↓      ↓      ↓      ↓
   ┌─────┐ ┌────┐ ┌────┐ ┌─────┐ ┌────┐
   │Auth │ │User│ │Inc │ │ Geo │ │Anl │
   │8081 │ │8082│ │8083│ │8084 │ │8085│
   └──┬──┘ └─┬──┘ └──┬─┘ └──┬──┘ └──┬─┘
      └───────┴───────┴───────┴───────┘
                     │
                     ↓
          ┌────────────────────┐
          │ PostgreSQL + PostGIS│
          │     (Port 5432)     │
          └────────────────────┘

        EUREKA SERVER (8761)
        ─────────────────────
        Tracks all services
```

**Benefits:**
- Each service can be developed independently
- Scale individual services as needed
- If one service fails, others keep running
- Different teams can work on different services

### 1.3 The Services Explained

| Service | Port | Purpose | Example |
|---------|------|---------|---------|
| **Eureka Server** | 8761 | Service discovery - knows where all services are | Dashboard showing all running services |
| **API Gateway** | 8080 | Front door - routes all requests | Client makes ONE request here, gateway forwards it |
| **Auth Service** | 8081 | Login, logout, JWT tokens | Officer logs in, gets a token |
| **User Service** | 8082 | Manage users and hierarchy | Create officers, assign to stations |
| **Incident Service** | 8083 | Crime reporting | Officer reports a robbery with GPS location |
| **Geographic Service** | 8084 | Spatial operations | Check if crime is within police station boundary |
| **Analytics Service** | 8085 | Statistics and reports | How many robberies this month? |

### Quiz 1: Test Your Understanding

**Q1**: What is the main benefit of using microservices instead of a monolithic application?

<details>
<summary>Click to see answer</summary>

**Answer**: Multiple correct answers:
- Services can be developed and deployed independently
- Better scalability (scale only what needs it)
- Fault isolation (one service failing doesn't crash everything)
- Technology flexibility (can use different tools per service)
</details>

**Q2**: Which service would handle a user login request?

<details>
<summary>Click to see answer</summary>

**Answer**: Auth Service (port 8081). It handles authentication and generates JWT tokens.
</details>

**Q3**: What port does the API Gateway run on, and why is it important?

<details>
<summary>Click to see answer</summary>

**Answer**: Port 8080. It's important because clients only need to know ONE address. The gateway handles routing to all the other services internally.
</details>

### Exercise 1: Explore the System

Open your browser and visit:

1. **Eureka Dashboard**: http://localhost:8761
   - What services are registered?
   - What is their status?

2. **API Gateway**: http://localhost:8080
   - What happens when you visit this URL?

3. **Swagger UI for each service**:
   - Auth: http://localhost:8081/swagger-ui.html
   - User: http://localhost:8082/swagger-ui.html
   - Incident: http://localhost:8083/swagger-ui.html

**Task**: Take a screenshot of the Eureka dashboard showing all registered services.

---

## Lesson 2: Setting Up Your Development Environment

### What You'll Learn
- Install required tools
- Start the backend
- Verify everything is working
- Common troubleshooting

### 2.1 Required Tools

#### Essential Tools

```bash
# 1. Check Java version (need 21+)
java -version

# 2. Check Maven (need 3.9+)
mvn -version

# 3. Check PostgreSQL
sudo service postgresql status

# 4. Check Git
git --version
```

#### Recommended IDE Setup

**Option 1: IntelliJ IDEA (Recommended)**
1. Download: https://www.jetbrains.com/idea/download/
2. Install Spring Boot plugin
3. Import project: File → Open → Select `/workspace`
4. Wait for Maven dependencies to download

**Option 2: VS Code**
1. Install extensions:
   - Language Support for Java
   - Spring Boot Extension Pack
   - Maven for Java

**Option 3: Eclipse**
1. Download Spring Tool Suite (STS)
2. Import Maven project

### 2.2 Starting the Backend

#### Step-by-Step Startup

**Terminal 1: Start Database**
```bash
# Start PostgreSQL
sudo service postgresql start

# Verify it's running
sudo -u postgres psql -d nisircop -c "SELECT COUNT(*) FROM users;"
```

**Terminal 2: Start Eureka Server**
```bash
cd backend/eureka-server
mvn spring-boot:run

# Wait until you see:
# "Started EurekaServerApplication in X seconds"
```

**Terminal 3: Start API Gateway**
```bash
cd backend/api-gateway
mvn spring-boot:run

# Wait for startup
```

**Terminals 4-8: Start Business Services**
```bash
# Set environment variables first
export POSTGRES_PASSWORD=nisircop_secure_password_2024
export JWT_SECRET=$(cat .env | grep JWT_SECRET | cut -d'=' -f2)

# Terminal 4
cd backend/auth-service && mvn spring-boot:run

# Terminal 5
cd backend/user-service && mvn spring-boot:run

# Terminal 6
cd backend/incident-service && mvn spring-boot:run

# Terminal 7
cd backend/geographic-service && mvn spring-boot:run

# Terminal 8
cd backend/analytics-service && mvn spring-boot:run
```

### 2.3 Verification Checklist

```bash
# 1. Check all services are registered
curl http://localhost:8761/eureka/apps | grep "<app>"

# Expected output:
# <app>API-GATEWAY</app>
# <app>AUTH-SERVICE</app>
# <app>USER-SERVICE</app>
# <app>INCIDENT-SERVICE</app>
# <app>GEOGRAPHIC-SERVICE</app>
# <app>ANALYTICS-SERVICE</app>

# 2. Test database connection
psql -h localhost -U postgres -d nisircop -c "SELECT postgis_version();"

# 3. Check Swagger UI
# Open browser to: http://localhost:8081/swagger-ui.html
```

### 2.4 Common Issues and Solutions

#### Issue 1: Port Already in Use

**Symptom**: `Port 8081 was already in use`

**Solution**:
```bash
# Find process using the port
lsof -i :8081

# Kill the process
kill -9 <PID>

# Or kill all Java processes
pkill -9 java
```

#### Issue 2: Database Connection Error

**Symptom**: `Connection to localhost:5432 refused`

**Solution**:
```bash
# Start PostgreSQL
sudo service postgresql start

# Check if running
sudo service postgresql status

# Check if nisircop database exists
sudo -u postgres psql -l | grep nisircop
```

#### Issue 3: Maven Build Fails

**Symptom**: `BUILD FAILURE` with dependency errors

**Solution**:
```bash
# Clear Maven cache and rebuild
mvn clean install -U

# Or force update
mvn clean install -U -DskipTests
```

### Exercise 2: Environment Setup Challenge

**Task 1**: Start all 7 microservices and take a screenshot of your terminal windows.

**Task 2**: Create a script to start all services automatically:

```bash
#!/bin/bash
# start-all-services.sh

echo "Starting NISIRCOP-LE Backend..."

# Set environment
export POSTGRES_PASSWORD=nisircop_secure_password_2024
export JWT_SECRET=$(cat .env | grep JWT_SECRET | cut -d'=' -f2)

# Start Eureka
cd backend/eureka-server && nohup mvn spring-boot:run > /tmp/eureka.log 2>&1 &
sleep 30

# Start Gateway
cd ../api-gateway && nohup mvn spring-boot:run > /tmp/gateway.log 2>&1 &
sleep 20

# Start services
cd ../auth-service && nohup mvn spring-boot:run > /tmp/auth.log 2>&1 &
cd ../user-service && nohup mvn spring-boot:run > /tmp/user.log 2>&1 &
cd ../incident-service && nohup mvn spring-boot:run > /tmp/incident.log 2>&1 &
cd ../geographic-service && nohup mvn spring-boot:run > /tmp/geographic.log 2>&1 &
cd ../analytics-service && nohup mvn spring-boot:run > /tmp/analytics.log 2>&1 &

echo "All services started! Check logs in /tmp/*.log"
```

---

## Lesson 3: Understanding the Core Architecture

### What You'll Learn
- Spring Boot fundamentals
- How Eureka service discovery works
- API Gateway routing
- Configuration management

### 3.1 Spring Boot Basics

Every microservice is a **Spring Boot application**. Here's the minimal structure:

```java
@SpringBootApplication
@EnableDiscoveryClient  // Registers with Eureka
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
```

**What happens when you run this?**

1. Spring Boot starts an embedded Tomcat server
2. Scans for `@Component`, `@Service`, `@Controller` annotations
3. Registers with Eureka Server
4. Starts listening on configured port

### 3.2 Configuration with application.properties

Every service has `src/main/resources/application.properties`:

```properties
# Service identity
spring.application.name=auth-service
server.port=8081

# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/nisircop
spring.datasource.username=postgres
spring.datasource.password=${POSTGRES_PASSWORD}

# Eureka registration
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka

# JWT configuration
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

**Key Concepts:**

- `${POSTGRES_PASSWORD}`: Reads from environment variable
- `spring.application.name`: How other services find this service
- `server.port`: What port to listen on

### 3.3 Service Discovery with Eureka

**How it works:**

```
1. Service starts up
   ↓
2. Registers with Eureka: "I'm auth-service at localhost:8081"
   ↓
3. Sends heartbeat every 30 seconds: "I'm still alive!"
   ↓
4. Other services query Eureka: "Where is auth-service?"
   ↓
5. Eureka responds: "It's at localhost:8081"
```

**View in action:**
```bash
# See all registered services
curl http://localhost:8761/eureka/apps

# See specific service
curl http://localhost:8761/eureka/apps/AUTH-SERVICE
```

### 3.4 API Gateway Routing

The gateway uses **Spring Cloud Gateway** with route configurations:

```properties
# Route for auth service
spring.cloud.gateway.routes[0].id=auth-service
spring.cloud.gateway.routes[0].uri=lb://auth-service  # lb = load balanced
spring.cloud.gateway.routes[0].predicates[0]=Path=/auth/**

# This means:
# Request to: http://localhost:8080/auth/login
# Gets routed to: http://localhost:8081/auth/login
```

**Load Balancing:**
- `lb://auth-service` means "use Eureka to find auth-service"
- If multiple instances are running, it balances between them

### 3.5 Maven Project Structure

```
backend/auth-service/
├── pom.xml                 # Maven configuration (dependencies)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/auth_service/
│   │   │       └── AuthServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── AuthServiceApplicationTests.java
└── target/                 # Compiled code (after mvn package)
    └── auth-service-0.0.1-SNAPSHOT.jar
```

**Important files:**

- `pom.xml`: Lists dependencies (Spring Boot, Spring Cloud, PostgreSQL driver, etc.)
- `Application.java`: Main class that starts the service
- `application.properties`: Configuration
- `target/*.jar`: Executable JAR file

### Quiz 3: Architecture Understanding

**Q1**: What annotation makes a Spring Boot application register with Eureka?

<details>
<summary>Answer</summary>

`@EnableDiscoveryClient` - This annotation tells Spring Boot to register this service with the Eureka Server.
</details>

**Q2**: If you have 3 instances of auth-service running, what happens when a request comes through the gateway?

<details>
<summary>Answer</summary>

The gateway uses load balancing (because of `lb://auth-service`). It will distribute requests across all 3 instances in a round-robin fashion, improving performance and reliability.
</details>

**Q3**: Where does Spring Boot look for the database password?

<details>
<summary>Answer</summary>

It looks in environment variables for `POSTGRES_PASSWORD` (specified by `${POSTGRES_PASSWORD}` in application.properties). You must set this before starting the service.
</details>

### Exercise 3: Configuration Challenge

**Task 1**: Change the auth-service port to 9081

1. Edit `backend/auth-service/src/main/resources/application.properties`
2. Change `server.port=8081` to `server.port=9081`
3. Restart the service
4. Verify it registered with Eureka on the new port

**Task 2**: Add a custom property

1. Add to application.properties: `app.name=NISIRCOP-LE Auth Service`
2. Create a REST controller that returns this value
3. Test it with curl

---

## Lesson 4: How Requests Flow Through the System

### What You'll Learn
- Complete request lifecycle
- JWT authentication flow
- Inter-service communication
- Error handling and resilience

### 4.1 Request Lifecycle

Let's trace a complete request from client to database and back:

```
1. CLIENT sends request
   POST http://localhost:8080/auth/login
   Body: {"username": "super_user", "password": "password"}
   
   ↓

2. API GATEWAY receives it
   - Checks route configuration
   - Finds: /auth/** → lb://auth-service
   - Asks Eureka: "Where is auth-service?"
   - Eureka responds: "localhost:8081"
   
   ↓

3. GATEWAY forwards to AUTH-SERVICE
   POST http://localhost:8081/auth/login
   
   ↓

4. AUTH-SERVICE processes
   - Controller receives request
   - Service layer validates credentials
   - Queries database: SELECT * FROM users WHERE username=?
   - Password matches? Generate JWT token
   
   ↓

5. DATABASE responds
   Returns user data
   
   ↓

6. AUTH-SERVICE sends response back
   {"token": "eyJhbGc...", "userId": 1, "role": "SUPER_USER"}
   
   ↓

7. GATEWAY forwards to CLIENT
   Client receives token, stores it for future requests
```

### 4.2 JWT Authentication Flow

**What is JWT?**

JSON Web Token - a secure way to prove who you are without sending password every time.

```
JWT Structure:
header.payload.signature

Example:
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.  ← header
eyJzdWIiOiJzdXBlcl91c2VyIiwicm9sZSI6IlNVUEVSX1VTRVIifQ.  ← payload  
4x7f9K3h2B8p1M6n  ← signature
```

**Authentication Flow:**

```bash
# Step 1: Login (get token)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"super_user","password":"password"}'

# Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400,
  "userId": 1,
  "role": "SUPER_USER"
}

# Step 2: Use token for authenticated requests
curl -X GET http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**What happens when you use the token:**

```
1. Client sends: Authorization: Bearer <token>
   ↓
2. Gateway validates:
   - Is token format valid?
   - Is signature correct (using JWT_SECRET)?
   - Is token expired?
   ↓
3. If valid: Forward to service with user info
   If invalid: Return 401 Unauthorized
```

### 4.3 Inter-Service Communication

Services can call each other using **RestTemplate** or **WebClient**:

```java
// In User Service, calling Geographic Service
@Service
public class UserService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public boolean isUserInBoundary(Long userId, Long boundaryId) {
        // Call geographic-service
        String url = "http://geographic-service/api/v1/geo/validate";
        
        ValidationRequest request = new ValidationRequest(userId, boundaryId);
        ValidationResponse response = restTemplate.postForObject(
            url, 
            request, 
            ValidationResponse.class
        );
        
        return response.isValid();
    }
}
```

**Note**: `http://geographic-service` works because of Eureka + LoadBalancer!

### 4.4 Database Transaction Flow

```java
// In Incident Service
@Service
public class IncidentService {
    
    @Autowired
    private IncidentRepository repository;
    
    @Transactional  // ← Ensures all-or-nothing
    public Incident createIncident(IncidentDTO dto) {
        // 1. Create incident entity
        Incident incident = new Incident();
        incident.setTitle(dto.getTitle());
        incident.setLocation(createPoint(dto.getLat(), dto.getLng()));
        
        // 2. Save to database
        incident = repository.save(incident);
        
        // 3. If this fails, step 2 is rolled back
        notifyOfficers(incident);
        
        return incident;
    }
    
    private Point createPoint(double lat, double lng) {
        GeometryFactory gf = new GeometryFactory();
        return gf.createPoint(new Coordinate(lng, lat));
    }
}
```

### 4.5 Error Handling

**Graceful degradation:**

```java
@Service
public class AnalyticsService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public List<IncidentStats> getStats() {
        try {
            // Try to get data from incident-service
            return restTemplate.getForObject(
                "http://incident-service/api/v1/incidents/stats",
                IncidentStats[].class
            );
        } catch (Exception e) {
            // Service is down, return cached data
            log.error("Incident service unavailable", e);
            return getCachedStats();
        }
    }
}
```

### Quiz 4: Request Flow

**Q1**: In what order do services start up for optimal operation?

<details>
<summary>Answer</summary>

1. PostgreSQL (database must be ready first)
2. Eureka Server (services need to register)
3. API Gateway (needs Eureka to find services)
4. Business services (auth, user, incident, etc.) - order doesn't matter

</details>

**Q2**: A JWT token expires in 24 hours. What happens after that?

<details>
<summary>Answer</summary>

The token becomes invalid. When the client tries to use it, the gateway will reject it with 401 Unauthorized. The client must login again to get a new token.

</details>

**Q3**: If the geographic-service is down, how does it affect other services?

<details>
<summary>Answer</summary>

Only requests that specifically need geographic data will fail. Other services continue working normally. This is the benefit of microservices - fault isolation.

</details>

### Exercise 4: Tracing a Request

**Task**: Add logging to trace a request through the system:

1. Add to each service's `application.properties`:
```properties
logging.level.com.example=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
```

2. Make a request and watch logs in all services

3. Document the flow you observed

---

## Lesson 5: Working with the Database

### What You'll Learn
- PostgreSQL with PostGIS basics
- JPA and Hibernate
- Spatial queries
- Database migrations

### 5.1 Database Schema Overview

```sql
-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,  -- ENUM: OFFICER, POLICE_STATION, SUPER_USER
    full_name VARCHAR(100),
    station_id INT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Incidents table with PostGIS
CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    incident_type VARCHAR(100),
    priority incident_priority NOT NULL,  -- ENUM: LOW, MEDIUM, HIGH, CRITICAL
    location GEOMETRY(Point, 4326) NOT NULL,  -- GPS coordinates
    reported_by INT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Spatial index for fast geographic queries
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);
```

### 5.2 JPA Entities

**User Entity:**

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;  // BCrypt hashed
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "station_id")
    private Integer stationId;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    // Getters and setters
}

enum UserRole {
    OFFICER, POLICE_STATION, SUPER_USER
}
```

**Incident Entity with PostGIS:**

```java
@Entity
@Table(name = "incidents")
public class Incident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "incident_type")
    private String incidentType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentPriority priority;
    
    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    private Point location;  // PostGIS Point type
    
    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    // Getters and setters
}
```

### 5.3 Repository Layer

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA generates implementation automatically!
    
    Optional<User> findByUsername(String username);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByStationId(Integer stationId);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.stationId = :stationId")
    List<User> findOfficersByStation(
        @Param("role") UserRole role, 
        @Param("stationId") Integer stationId
    );
}
```

**How to use:**

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    }
    
    public List<User> getOfficersAtStation(Integer stationId) {
        return userRepository.findOfficersByStation(UserRole.OFFICER, stationId);
    }
}
```

### 5.4 Spatial Queries with PostGIS

**Point-in-Polygon Check:**

```java
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    
    // Find incidents within a boundary
    @Query(value = """
        SELECT i.* FROM incidents i
        JOIN boundaries b ON b.id = :boundaryId
        WHERE ST_Within(i.location, b.geom)
        """, nativeQuery = true)
    List<Incident> findIncidentsInBoundary(@Param("boundaryId") Long boundaryId);
    
    // Find incidents within distance (meters)
    @Query(value = """
        SELECT i.* FROM incidents i
        WHERE ST_DWithin(
            i.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :distanceMeters
        )
        ORDER BY ST_Distance(
            i.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
        )
        """, nativeQuery = true)
    List<Incident> findIncidentsNear(
        @Param("lat") double latitude,
        @Param("lng") double longitude,
        @Param("distanceMeters") double distance
    );
}
```

**Creating a Point:**

```java
public Incident createIncident(double latitude, double longitude) {
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    Point location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    
    Incident incident = new Incident();
    incident.setLocation(location);
    // ... set other fields
    
    return incidentRepository.save(incident);
}
```

### 5.5 Database Operations

**Connect to database:**

```bash
psql -h localhost -U postgres -d nisircop

# Inside psql:
\dt                          # List tables
\d users                     # Describe users table
SELECT * FROM users;         # Query data
```

**Common queries:**

```sql
-- Get all users
SELECT id, username, role, full_name FROM users;

-- Get incidents with coordinates
SELECT 
    id, 
    title, 
    ST_Y(location) as latitude,
    ST_X(location) as longitude
FROM incidents;

-- Find incidents within 1km of a point
SELECT 
    id,
    title,
    ST_Distance(
        location::geography,
        ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)::geography
    ) as distance_meters
FROM incidents
WHERE ST_DWithin(
    location::geography,
    ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)::geography,
    1000
)
ORDER BY distance_meters;
```

### Quiz 5: Database Operations

**Q1**: What is the difference between `save()` and `saveAndFlush()` in JPA?

<details>
<summary>Answer</summary>

- `save()`: Persists entity but may not immediately write to database (batches writes)
- `saveAndFlush()`: Immediately writes to database and flushes the session

Use `saveAndFlush()` when you need the database-generated ID immediately.

</details>

**Q2**: How do you create a Point in PostGIS with latitude 9.0192 and longitude 38.7525?

<details>
<summary>Answer</summary>

```sql
ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)
```

**Important**: Longitude comes first, then latitude! SRID 4326 is WGS 84 coordinate system.

</details>

### Exercise 5: Database Challenge

**Task 1**: Create a new entity and repository

1. Create `Boundary` entity:
```java
@Entity
@Table(name = "boundaries")
public class Boundary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Polygon geom;
    
    // Getters and setters
}
```

2. Create `BoundaryRepository`

3. Add a method to find boundaries by name

**Task 2**: Write a spatial query

Write a query to find all incidents that occurred in the last 7 days within 500 meters of a given point.

---

## Lesson 6: Adding New Features

### What You'll Learn
- Creating REST controllers
- Request/response DTOs
- Service layer patterns
- Error handling
- API documentation with Swagger

### 6.1 REST Controller Basics

**Example: User Controller**

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // GET /api/v1/users - List all users
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users in the system")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> dtos = users.stream()
            .map(this::convertToDTO)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    // GET /api/v1/users/{id} - Get user by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }
    
    // POST /api/v1/users - Create new user
    @PostMapping
    @Operation(summary = "Create new user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(user));
    }
    
    // PUT /api/v1/users/{id} - Update user
    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        User user = userService.updateUser(id, request);
        return ResponseEntity.ok(convertToDTO(user));
    }
    
    // DELETE /api/v1/users/{id} - Delete user
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getRole(),
            user.getFullName(),
            user.getStationId()
        );
    }
}
```

### 6.2 DTOs (Data Transfer Objects)

**Why DTOs?**
- Don't expose database entities directly
- Control exactly what data is sent/received
- Add validation
- Version your API independently

**Request DTO:**

```java
public class CreateIncidentRequest {
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @NotNull(message = "Incident type is required")
    private String incidentType;
    
    @NotNull(message = "Priority is required")
    private IncidentPriority priority;
    
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;
    
    @Past(message = "Incident must have occurred in the past")
    private LocalDateTime occurredAt;
    
    // Getters and setters
}
```

**Response DTO:**

```java
public record IncidentDTO(
    Long id,
    String title,
    String description,
    String incidentType,
    IncidentPriority priority,
    LocationDTO location,
    Long reportedBy,
    LocalDateTime createdAt,
    LocalDateTime occurredAt
) {}

public record LocationDTO(
    double latitude,
    double longitude
) {}
```

### 6.3 Service Layer Pattern

```java
@Service
@Transactional
public class IncidentService {
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GeographicServiceClient geographicClient;  // Feign client
    
    public Incident createIncident(CreateIncidentRequest request, Long userId) {
        // 1. Validate user exists
        User reporter = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        // 2. Validate user is OFFICER role
        if (reporter.getRole() != UserRole.OFFICER) {
            throw new UnauthorizedException("Only officers can report incidents");
        }
        
        // 3. Validate location is within officer's jurisdiction
        if (!geographicClient.validateLocation(
                reporter.getStationId(), 
                request.getLatitude(), 
                request.getLongitude())) {
            throw new OutOfJurisdictionException("Location is outside your jurisdiction");
        }
        
        // 4. Create incident
        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setIncidentType(request.getIncidentType());
        incident.setPriority(request.getPriority());
        incident.setLocation(createPoint(request.getLatitude(), request.getLongitude()));
        incident.setReportedBy(reporter);
        incident.setOccurredAt(request.getOccurredAt());
        
        // 5. Save and return
        return incidentRepository.save(incident);
    }
    
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByStation(Integer stationId) {
        // Complex query that joins incidents with boundaries
        return incidentRepository.findByStationBoundary(stationId);
    }
    
    private Point createPoint(double lat, double lng) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        return gf.createPoint(new Coordinate(lng, lat));
    }
}
```

### 6.4 Error Handling

**Global Exception Handler:**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ValidationErrorResponse response = new ValidationErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors,
            LocalDateTime.now()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### 6.5 Swagger Documentation

**Configure Swagger:**

```java
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NISIRCOP-LE Incident Service API")
                .version("1.0")
                .description("API for managing crime incidents")
                .contact(new Contact()
                    .name("NISIRCOP Development Team")
                    .email("dev@nisircop.et")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
```

**Document endpoints:**

```java
@GetMapping("/search")
@Operation(
    summary = "Search incidents",
    description = "Search incidents by various criteria including type, priority, date range, and location"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully retrieved incidents"),
    @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
})
public ResponseEntity<List<IncidentDTO>> searchIncidents(
        @Parameter(description = "Incident type (e.g., ROBBERY, VANDALISM)")
        @RequestParam(required = false) String type,
        
        @Parameter(description = "Minimum priority level")
        @RequestParam(required = false) IncidentPriority minPriority,
        
        @Parameter(description = "Start date (ISO 8601 format)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
        LocalDateTime startDate,
        
        @Parameter(description = "End date (ISO 8601 format)")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
        LocalDateTime endDate) {
    
    List<Incident> incidents = incidentService.search(type, minPriority, startDate, endDate);
    return ResponseEntity.ok(convertToDTOs(incidents));
}
```

### Quiz 6: Feature Development

**Q1**: Why should you use DTOs instead of returning entities directly?

<details>
<summary>Answer</summary>

Multiple reasons:
1. Security - Don't expose sensitive fields (like passwords)
2. API Versioning - Change database without breaking API
3. Performance - Only send data clients need
4. Documentation - Clear contract of what data is exchanged
5. Validation - Add input validation rules

</details>

**Q2**: What is `@Transactional` and when should you use it?

<details>
<summary>Answer</summary>

`@Transactional` ensures database operations are atomic (all-or-nothing):

```java
@Transactional
public void transferIncident(Long incidentId, Long newOfficerId) {
    // If ANY of these fail, ALL are rolled back
    incident.setAssignedTo(newOfficer);
    incidentRepository.save(incident);
    
    createNotification(newOfficer);
    updateStatistics();
}
```

Use it on service methods that perform multiple database operations that must succeed together.

</details>

### Exercise 6: Build a Feature

**Task**: Implement a complete "Incident Comments" feature

1. **Create Database Table:**
```sql
CREATE TABLE incident_comments (
    id SERIAL PRIMARY KEY,
    incident_id INT NOT NULL REFERENCES incidents(id),
    user_id INT NOT NULL REFERENCES users(id),
    comment TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

2. **Create Entity:**
```java
@Entity
@Table(name = "incident_comments")
public class IncidentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
```

3. **Create Repository**

4. **Create Service**

5. **Create Controller with endpoints:**
   - `GET /api/v1/incidents/{incidentId}/comments` - List comments
   - `POST /api/v1/incidents/{incidentId}/comments` - Add comment
   - `DELETE /api/v1/incidents/{incidentId}/comments/{commentId}` - Delete comment

6. **Test with Swagger UI**

---

## Lesson 7: Testing and Debugging

### What You'll Learn
- Unit testing with JUnit
- Integration testing
- Testing with Swagger UI
- Debugging techniques
- Logging best practices

### 7.1 Unit Testing

**Test a Service Method:**

```java
@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {
    
    @Mock
    private IncidentRepository incidentRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private IncidentService incidentService;
    
    @Test
    void createIncident_validRequest_success() {
        // Arrange
        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setTitle("Test Incident");
        request.setLatitude(9.0192);
        request.setLongitude(38.7525);
        request.setPriority(IncidentPriority.HIGH);
        
        User officer = new User();
        officer.setId(1L);
        officer.setRole(UserRole.OFFICER);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(officer));
        when(incidentRepository.save(any(Incident.class)))
            .thenAnswer(invocation -> {
                Incident saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });
        
        // Act
        Incident result = incidentService.createIncident(request, 1L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Incident", result.getTitle());
        assertEquals(IncidentPriority.HIGH, result.getPriority());
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }
    
    @Test
    void createIncident_nonOfficerUser_throwsException() {
        // Arrange
        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setTitle("Test");
        
        User admin = new User();
        admin.setId(1L);
        admin.setRole(UserRole.SUPER_USER);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            incidentService.createIncident(request, 1L);
        });
    }
}
```

**Test a Controller:**

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void getAllUsers_returnsUserList() throws Exception {
        // Arrange
        List<UserDTO> users = Arrays.asList(
            new UserDTO(1L, "user1", UserRole.OFFICER, "John Doe", 1),
            new UserDTO(2L, "user2", UserRole.OFFICER, "Jane Smith", 1)
        );
        
        when(userService.findAll()).thenReturn(users);
        
        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].username").value("user1"))
            .andExpect(jsonPath("$[1].username").value("user2"));
    }
}
```

### 7.2 Integration Testing

**Test with Real Database:**

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers  // Uses Docker containers for testing
class IncidentRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgis/postgis:15-3.3")
        .withDatabaseName("test_nisircop")
        .withUsername("test")
        .withPassword("test");
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Test
    void findIncidentsNear_returnsIncidentsWithinDistance() {
        // Arrange
        Incident incident1 = createIncidentAt(9.0192, 38.7525);  // Addis Ababa center
        Incident incident2 = createIncidentAt(9.0300, 38.7600);  // ~1.5km away
        Incident incident3 = createIncidentAt(9.1000, 38.8000);  // ~10km away
        
        incidentRepository.saveAll(Arrays.asList(incident1, incident2, incident3));
        
        // Act - search within 2km radius
        List<Incident> results = incidentRepository.findIncidentsNear(
            9.0192, 38.7525, 2000.0
        );
        
        // Assert
        assertEquals(2, results.size());
        assertTrue(results.contains(incident1));
        assertTrue(results.contains(incident2));
        assertFalse(results.contains(incident3));
    }
}
```

### 7.3 Testing with Swagger UI

**Step-by-step:**

1. **Open Swagger UI**: http://localhost:8081/swagger-ui.html

2. **Authorize (if needed)**:
   - Click "Authorize" button
   - Enter: `Bearer your_jwt_token_here`
   - Click "Authorize"

3. **Test an endpoint**:
   - Expand `POST /api/v1/incidents`
   - Click "Try it out"
   - Fill in the request body:
```json
{
  "title": "Test Robbery",
  "description": "Testing the API",
  "incidentType": "ROBBERY",
  "priority": "HIGH",
  "latitude": 9.0192,
  "longitude": 38.7525,
  "occurredAt": "2024-10-14T15:30:00Z"
}
```
   - Click "Execute"
   - Check response code and body

4. **Test with different inputs**:
   - Invalid latitude (e.g., 200) - should return 400
   - Missing required field - should return 400
   - Valid data - should return 201

### 7.4 Debugging Techniques

**Enable Debug Logging:**

```properties
# application.properties
logging.level.com.example.incident_service=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# This will show:
# - All SQL queries
# - Parameter values
# - Service method calls
```

**Add Strategic Logging:**

```java
@Service
@Slf4j  // Lombok annotation
public class IncidentService {
    
    public Incident createIncident(CreateIncidentRequest request, Long userId) {
        log.debug("Creating incident: title={}, userId={}", request.getTitle(), userId);
        
        User reporter = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.error("User not found: userId={}", userId);
                return new UserNotFoundException(userId);
            });
        
        log.debug("Found reporter: username={}, role={}", 
                  reporter.getUsername(), reporter.getRole());
        
        // ... rest of method
        
        Incident saved = incidentRepository.save(incident);
        log.info("Incident created: id={}, title={}", saved.getId(), saved.getTitle());
        
        return saved;
    }
}
```

**Use IntelliJ Debugger:**

1. Set breakpoint (click left margin)
2. Run in Debug mode
3. Step through code (F8 = next line, F7 = step into)
4. Evaluate expressions (Alt+F8)
5. Watch variables

**Common Issues and Solutions:**

| Problem | Symptom | Solution |
|---------|---------|----------|
| Service not registering | Not appearing in Eureka dashboard | Check `@EnableDiscoveryClient` annotation and Eureka URL |
| Database connection error | `Connection refused` | Check PostgreSQL is running, credentials correct |
| 404 Not Found | Endpoint not found | Check `@RequestMapping` path, ensure controller is scanned |
| 500 Internal Error | Server error | Check logs for stack trace, add debug logging |
| JWT token rejected | 401 Unauthorized | Check token hasn't expired, JWT_SECRET is same across services |

### 7.5 Performance Testing

**Load Test with JMeter or similar:**

```bash
# Install Apache Bench (ab)
sudo apt-get install apache2-utils

# Test GET endpoint (100 requests, 10 concurrent)
ab -n 100 -c 10 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  http://localhost:8080/api/v1/incidents

# Results:
# Requests per second
# Average response time
# Failure rate
```

**Database Query Performance:**

```sql
-- Check slow queries
EXPLAIN ANALYZE 
SELECT i.* FROM incidents i
JOIN boundaries b ON ST_Within(i.location, b.geom)
WHERE b.id = 1;

-- Look for:
-- - Sequential Scan (bad)
-- - Index Scan (good)
-- - Execution time
```

### Quiz 7: Testing

**Q1**: What's the difference between `@Mock` and `@InjectMocks`?

<details>
<summary>Answer</summary>

- `@Mock`: Creates a mock (fake) object
- `@InjectMocks`: Creates real object and injects mocks into it

Example:
```java
@Mock
private UserRepository userRepository;  // Fake repository

@InjectMocks
private UserService userService;  // Real service with fake repository injected
```

</details>

**Q2**: When should you use integration tests vs unit tests?

<details>
<summary>Answer</summary>

**Unit Tests**: Test individual methods in isolation
- Fast (no database, no network)
- Test business logic
- Use mocks for dependencies

**Integration Tests**: Test components working together
- Slower (uses real database/services)
- Test actual SQL queries, API calls
- Catch integration issues

Use both! Unit tests for development speed, integration tests for confidence.

</details>

### Exercise 7: Testing Challenge

**Task 1**: Write comprehensive tests for IncidentService

1. Test successful incident creation
2. Test validation errors (invalid coordinates, missing fields)
3. Test authorization (non-officer trying to create incident)
4. Test database error handling

**Task 2**: Load test your API

1. Create 1000 test incidents
2. Run load test with 50 concurrent users
3. Identify bottlenecks
4. Optimize slow queries

---

## Final Project

### Build a Complete Feature: Incident Assignment System

**Requirements:**

1. **Database Schema**
   - Add `assigned_to` column to incidents table
   - Track assignment history

2. **Business Logic**
   - Station commanders can assign incidents to officers
   - Officers can't assign incidents
   - Can't assign to officers from different stations

3. **APIs**
   - `POST /api/v1/incidents/{id}/assign` - Assign incident
   - `GET /api/v1/incidents/my-assignments` - Get my assigned incidents
   - `GET /api/v1/incidents/{id}/assignment-history` - View history

4. **Features**
   - Email notification when incident assigned (simulate)
   - Validation of officer jurisdiction
   - Audit trail

5. **Testing**
   - Unit tests for business logic
   - Integration tests for database
   - Swagger documentation
   - Manual testing via Swagger UI

**Deliverables:**

1. Working code in all layers (entity, repository, service, controller)
2. Unit tests with >80% coverage
3. Integration tests
4. Swagger documentation
5. README explaining the feature

---

## Additional Resources

### Books
- "Spring in Action" by Craig Walls
- "Spring Microservices in Action" by John Carnell
- "Clean Code" by Robert C. Martin

### Online Courses
- Spring Framework Documentation: https://spring.io/guides
- Baeldung Spring Tutorials: https://www.baeldung.com/spring-tutorial
- PostGIS in Action: http://postgis.net/docs/

### Tools
- Postman: API testing
- DBeaver: Database management
- IntelliJ IDEA: IDE
- Docker: Containerization

### Community
- Stack Overflow: https://stackoverflow.com/questions/tagged/spring-boot
- Spring Community: https://spring.io/community
- GitHub Discussions

---

## Congratulations! 🎉

You've completed the NISIRCOP-LE Backend Learning Course!

You now know how to:
- ✅ Understand microservices architecture
- ✅ Set up a development environment
- ✅ Work with Spring Boot and Spring Cloud
- ✅ Use PostgreSQL with PostGIS for spatial data
- ✅ Build REST APIs with proper structure
- ✅ Test and debug effectively
- ✅ Add new features to the system

### Next Steps

1. **Practice**: Build the final project
2. **Explore**: Add your own features
3. **Contribute**: Improve the codebase
4. **Learn More**: Dive deeper into topics that interest you

**Happy Coding!** 🚀
