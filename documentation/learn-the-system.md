# NISIRCOP-LE-ANALYTICS: A Complete Course from Fundamentals to Mastery

> **A comprehensive guide to understanding modern full-stack web development through the lens of a real-world crime analytics platform**

---

## Table of Contents

1. [Part I: Foundation - Understanding the Basics](#part-i-foundation---understanding-the-basics)
2. [Part II: Backend Technologies](#part-ii-backend-technologies)
3. [Part III: Frontend Technologies](#part-iii-frontend-technologies)
4. [Part IV: Database & Geospatial Concepts](#part-iv-database--geospatial-concepts)
5. [Part V: Architecture & Design Patterns](#part-v-architecture--design-patterns)
6. [Part VI: The NISIRCOP System Deep Dive](#part-vi-the-nisircop-system-deep-dive)
7. [Part VII: Containerization & Deployment](#part-vii-containerization--deployment)
8. [Part VIII: Hands-On Exercises](#part-viii-hands-on-exercises)
9. [Part IX: Advanced Topics](#part-ix-advanced-topics)

---

# Part I: Foundation - Understanding the Basics

## Chapter 1: Web Development Fundamentals

### 1.1 What is Web Development?

Web development is the process of building applications that run on the internet or an intranet. These applications consist of two main parts:

- **Frontend (Client-Side)**: What users see and interact with in their web browser
- **Backend (Server-Side)**: The logic, database operations, and business rules that run on servers

**Real-World Analogy**: Think of a restaurant:
- The **frontend** is the dining area where customers sit, see the menu, and place orders
- The **backend** is the kitchen where chefs prepare meals, manage inventory, and handle orders

### 1.2 The Client-Server Model

The client-server model is the foundation of web applications:

```
┌─────────────┐         HTTP Request          ┌─────────────┐
│   Client    │ ──────────────────────────▶   │   Server    │
│  (Browser)  │                                │  (Backend)  │
│             │ ◀──────────────────────────    │             │
└─────────────┘         HTTP Response          └─────────────┘
```

**How It Works**:
1. The **client** (your web browser) sends a request to the server
2. The **server** processes the request, possibly querying a database
3. The server sends back a **response** with the requested data
4. The client displays the data to the user

### 1.3 HTTP: The Language of the Web

**HTTP (HyperText Transfer Protocol)** is how clients and servers communicate. Think of it as the "language" they speak.

**Common HTTP Methods**:
- **GET**: Retrieve data (like reading a book)
- **POST**: Send new data (like submitting a form)
- **PUT**: Update existing data (like editing a document)
- **DELETE**: Remove data (like deleting a file)

**Example**: When you report a crime incident in NISIRCOP:
```
POST /api/v1/incidents
{
  "title": "Robbery at Main St",
  "priority": "HIGH",
  "location": { "lat": 40.75, "lng": -73.95 }
}
```

### 1.4 APIs: Application Programming Interfaces

An **API** is a contract that defines how different software components communicate. In web development, we often use **REST APIs**.

**REST API Principles**:
- **Resources**: Everything is a resource (users, incidents, analytics)
- **URLs**: Each resource has a unique URL
- **Methods**: Use HTTP methods to perform actions
- **Stateless**: Each request contains all necessary information

**NISIRCOP Example**:
```
GET    /api/v1/users          → List all users
POST   /api/v1/users          → Create a new user
GET    /api/v1/users/123      → Get user with ID 123
PUT    /api/v1/users/123      → Update user 123
DELETE /api/v1/users/123      → Delete user 123
```

### 1.5 JSON: Data Format

**JSON (JavaScript Object Notation)** is the standard format for exchanging data between client and server.

**Example User Object**:
```json
{
  "id": 123,
  "username": "officer_jane",
  "role": "OFFICER",
  "fullName": "Jane Doe",
  "stationId": 1,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Why JSON?**
- Human-readable
- Easy for machines to parse
- Language-independent
- Lightweight

---

## Chapter 2: Understanding Software Architecture

### 2.1 Monolithic vs. Microservices

**Monolithic Architecture** (Traditional):
```
┌──────────────────────────────────┐
│     Single Application           │
│  ┌────────────────────────────┐  │
│  │  User Management           │  │
│  │  Incident Reporting        │  │
│  │  Analytics                 │  │
│  │  All in one codebase       │  │
│  └────────────────────────────┘  │
└──────────────────────────────────┘
```

**Pros**: Simple to develop initially, easy to deploy
**Cons**: Hard to scale, difficult to maintain as it grows, one bug can crash everything

**Microservices Architecture** (Modern):
```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│    User      │  │   Incident   │  │  Analytics   │
│   Service    │  │   Service    │  │   Service    │
│              │  │              │  │              │
└──────────────┘  └──────────────┘  └──────────────┘
     ▲                  ▲                  ▲
     └──────────────────┴──────────────────┘
              Independent Services
```

**Pros**: 
- Independent deployment
- Easier to scale specific services
- Technology flexibility
- Fault isolation

**Cons**: 
- More complex infrastructure
- Network communication overhead
- Distributed system challenges

**NISIRCOP Uses Microservices**: Each service (User, Incident, Analytics) runs independently.

### 2.2 Separation of Concerns

A fundamental principle: **divide your application into distinct sections, each handling a specific responsibility**.

**Three-Tier Architecture**:
```
┌─────────────────────────────────────┐
│  Presentation Layer (Frontend)     │ ← User Interface
├─────────────────────────────────────┤
│  Business Logic Layer (Backend)    │ ← Application Logic
├─────────────────────────────────────┤
│  Data Layer (Database)              │ ← Data Storage
└─────────────────────────────────────┘
```

---

# Part II: Backend Technologies

## Chapter 3: Java Programming Language

### 3.1 What is Java?

**Java** is a versatile, object-oriented programming language that runs on any platform with a Java Virtual Machine (JVM).

**Key Characteristics**:
- **Write Once, Run Anywhere**: Java code compiles to bytecode that runs on any JVM
- **Object-Oriented**: Everything is an object
- **Strongly Typed**: Variables must have explicit types
- **Memory Managed**: Automatic garbage collection

### 3.2 Java Basics

**Important**: NISIRCOP uses **Java 17 LTS** across all backend services. This is configured and locked in:
- `build.gradle`: `JavaLanguageVersion.of(17)`
- Docker build: `gradle:8.5-jdk17-alpine`
- Docker runtime: `eclipse-temurin:17-jre-alpine`

**Hello World Example**:
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, NISIRCOP!");
    }
}
```

**Key Concepts**:
- `public class`: Declares a class accessible from anywhere
- `static`: Method belongs to the class, not instances
- `void`: Method returns nothing
- `String[] args`: Command-line arguments

### 3.3 Object-Oriented Programming in Java

**Classes and Objects**:
```java
// Define a class (blueprint)
public class User {
    // Properties (fields)
    private Long id;
    private String username;
    private String role;
    
    // Constructor
    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }
    
    // Methods (behaviors)
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}

// Create an object (instance)
User officer = new User("officer_jane", "OFFICER");
System.out.println(officer.getUsername()); // Output: officer_jane
```

**The Four Pillars of OOP**:

1. **Encapsulation**: Bundle data and methods together, hide internal details
```java
public class User {
    private String password; // Private - hidden from outside
    
    public void setPassword(String password) {
        // Validate before setting
        if (password.length() >= 8) {
            this.password = password;
        }
    }
}
```

2. **Inheritance**: Create new classes based on existing ones
```java
public class Officer extends User {
    private String badgeNumber;
    
    public Officer(String username, String badgeNumber) {
        super(username, "OFFICER"); // Call parent constructor
        this.badgeNumber = badgeNumber;
    }
}
```

3. **Polymorphism**: Objects can take multiple forms
```java
public interface Reportable {
    void generateReport();
}

public class Incident implements Reportable {
    public void generateReport() {
        System.out.println("Incident Report");
    }
}

public class Analytics implements Reportable {
    public void generateReport() {
        System.out.println("Analytics Report");
    }
}
```

4. **Abstraction**: Hide complex implementation details
```java
public abstract class Service {
    public abstract void start();
    public abstract void stop();
}
```

### 3.4 Java Collections

Collections are data structures for storing multiple objects:

```java
import java.util.*;

// List - Ordered collection
List<String> roles = new ArrayList<>();
roles.add("OFFICER");
roles.add("POLICE_STATION");
roles.add("SUPER_USER");

// Map - Key-value pairs
Map<Long, User> users = new HashMap<>();
users.put(1L, new User("jane", "OFFICER"));
users.put(2L, new User("john", "SUPER_USER"));

// Access
User user = users.get(1L);
```

---

## Chapter 4: Spring Boot Framework

### 4.1 What is Spring Boot?

**Spring Boot** is a framework that simplifies building production-ready Java applications. It provides:
- **Auto-configuration**: Automatically configures your application
- **Embedded servers**: Run applications without external servers
- **Production-ready features**: Health checks, metrics, configuration

**Why Spring Boot?**
- Rapid development
- Reduced boilerplate code
- Built-in best practices
- Huge ecosystem

### 4.2 Spring Boot Application Structure

**Typical Structure**:
```
src/main/java/com/example/userservice/
├── UserServiceApplication.java    ← Main entry point
├── controller/                    ← Handle HTTP requests
│   └── UserController.java
├── service/                       ← Business logic
│   └── UserService.java
├── repository/                    ← Database access
│   └── UserRepository.java
└── model/                         ← Data models
    └── User.java
```

### 4.3 Spring Boot Annotations

Annotations are metadata that provide information to the framework:

**Application Entry Point**:
```java
@SpringBootApplication  // Marks the main class
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

**REST Controller**:
```java
@RestController  // Marks this as a REST API controller
@RequestMapping("/api/v1/users")  // Base URL path
public class UserController {
    
    @Autowired  // Inject dependency
    private UserService userService;
    
    @GetMapping  // Handle GET requests
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @PostMapping  // Handle POST requests
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
    
    @GetMapping("/{id}")  // URL parameter
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

**Service Layer**:
```java
@Service  // Marks this as a service component
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
}
```

**Data Model**:
```java
@Entity  // JPA entity - maps to database table
@Table(name = "users")
public class User {
    
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // Getters and setters...
}
```

### 4.4 Dependency Injection

**Dependency Injection (DI)** is a design pattern where objects receive their dependencies from external sources rather than creating them.

**Without DI** (Tight Coupling):
```java
public class UserController {
    private UserService userService = new UserService();  // BAD: Creates dependency
}
```

**With DI** (Loose Coupling):
```java
@RestController
public class UserController {
    
    @Autowired  // Spring injects the dependency
    private UserService userService;
    
    // OR use constructor injection (preferred)
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

**Benefits**:
- Easier testing (can inject mocks)
- Loose coupling
- Better code organization

### 4.5 Spring Data JPA

**JPA (Java Persistence API)** simplifies database operations. Spring Data JPA makes it even easier.

**Repository Interface**:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring automatically implements these methods!
    
    User findByUsername(String username);
    
    List<User> findByRole(Role role);
    
    @Query("SELECT u FROM User u WHERE u.stationId = :stationId")
    List<User> findByStation(@Param("stationId") Long stationId);
}
```

**No Implementation Needed**: Spring Data JPA generates the implementation at runtime!

**For Services with Geospatial Data** (Incident Service, Geographic Service):
```properties
# Use PostGIS dialect for spatial queries
spring.jpa.properties.hibernate.dialect=org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect
```

**Dependencies Required**:
```gradle
implementation 'org.hibernate.orm:hibernate-spatial:6.6.3.Final'
```

---

## Chapter 5: Spring Cloud & Microservices

### 5.1 Challenges of Microservices

When you split an application into multiple services, new challenges emerge:
- **Service Discovery**: How do services find each other?
- **API Gateway**: How do clients access multiple services?
- **Configuration**: How to manage configurations across services?
- **Load Balancing**: How to distribute requests?

**Spring Cloud** provides solutions to these challenges.

**Important**: NISIRCOP uses **Spring Cloud 2024.0.0** and **Spring Boot 3.5.6**, configured for compatibility.

### 5.2 Eureka: Service Discovery

**Problem**: In a microservices architecture, service instances can dynamically change (IP addresses, ports).

**Solution**: **Eureka Server** acts as a service registry.

```
┌──────────────┐
│    Eureka    │  ← Service Registry
│   Server     │
└──────┬───────┘
       │
   ┌───┴────┬──────────┬────────────┐
   │        │          │            │
┌──▼───┐ ┌─▼────┐ ┌───▼─────┐ ┌───▼──────┐
│ User │ │ Auth │ │Incident │ │Analytics │
│Service│ │Service│ │ Service │ │ Service  │
└──────┘ └──────┘ └─────────┘ └──────────┘
    ↑        All register with Eureka
```

**Eureka Server**:
```java
@SpringBootApplication
@EnableEurekaServer  // Enable Eureka Server
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

**Eureka Client** (Each microservice):
```java
@SpringBootApplication
@EnableEurekaClient  // Register with Eureka
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

**Configuration** (`application.properties`):
```properties
spring.application.name=user-service
server.port=8082

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/nisircop
spring.datasource.username=postgres
spring.datasource.password=${POSTGRES_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration (PRODUCTION SAFE)
spring.jpa.hibernate.ddl-auto=validate  # Only validate, never modify schema
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Eureka Configuration
eureka.client.service-url.defaultZone=http://eureka-server:8761/eureka
```

**Security Note**: Using `ddl-auto=validate` prevents accidental database schema modifications in production.

### 5.3 API Gateway

**Problem**: Clients would need to know the address of every microservice.

**Solution**: **API Gateway** is a single entry point for all client requests.

```
┌─────────┐
│ Client  │
└────┬────┘
     │
     ▼
┌────────────────┐
│  API Gateway   │  ← Single Entry Point
└────┬───────────┘
     │
  ┌──┴──┬──────────┬────────────┐
  │     │          │            │
┌─▼──┐ ┌▼───┐ ┌───▼─────┐ ┌───▼──────┐
│User│ │Auth│ │Incident │ │Analytics │
└────┘ └────┘ └─────────┘ └──────────┘
```

**API Gateway Configuration** (`application.properties`):
```properties
spring.application.name=api-gateway
server.port=8080

eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka

# Route configurations
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/users/**

spring.cloud.gateway.routes[1].id=incident-service
spring.cloud.gateway.routes[1].uri=lb://incident-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/v1/incidents/**

spring.cloud.gateway.routes[2].id=geographic-service
spring.cloud.gateway.routes[2].uri=lb://geographic-service
spring.cloud.gateway.routes[2].predicates[0]=Path=/api/v1/geo/**

spring.cloud.gateway.routes[3].id=analytics-service
spring.cloud.gateway.routes[3].uri=lb://analytics-service
spring.cloud.gateway.routes[3].predicates[0]=Path=/api/v1/analytics/**

spring.cloud.gateway.routes[4].id=auth-service
spring.cloud.gateway.routes[4].uri=lb://auth-service
spring.cloud.gateway.routes[4].predicates[0]=Path=/auth/**
```

**Application Class**:
```java
@SpringBootApplication
@EnableDiscoveryClient  // Required for service discovery
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

**Important**: Use `spring-cloud-starter-gateway` dependency (NOT `gateway-server-webmvc`).

**Benefits**:
- Simplified client code
- Centralized security
- Load balancing
- Request routing

---

# Part III: Frontend Technologies

## Chapter 6: JavaScript & Modern Web

### 6.1 JavaScript Basics

**JavaScript** is the programming language of the web. It runs in browsers and makes web pages interactive.

**Basic Syntax**:
```javascript
// Variables
let username = "officer_jane";
const API_URL = "http://localhost:8080/api";

// Functions
function greet(name) {
    return `Hello, ${name}!`;
}

// Arrow functions (modern syntax)
const greet = (name) => `Hello, ${name}!`;

// Objects
const user = {
    id: 1,
    username: "jane",
    role: "OFFICER"
};

// Arrays
const roles = ["OFFICER", "POLICE_STATION", "SUPER_USER"];

// Array methods
roles.forEach(role => console.log(role));
const officers = users.filter(u => u.role === "OFFICER");
```

### 6.2 Asynchronous JavaScript

Web applications need to make API calls without blocking the user interface.

**Promises**:
```javascript
// Making an API call
fetch('http://localhost:8080/api/v1/users')
    .then(response => response.json())
    .then(users => {
        console.log('Users:', users);
    })
    .catch(error => {
        console.error('Error:', error);
    });
```

**Async/Await** (Modern Syntax):
```javascript
async function loadUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/v1/users');
        const users = await response.json();
        console.log('Users:', users);
    } catch (error) {
        console.error('Error:', error);
    }
}
```

### 6.3 ES6+ Features

Modern JavaScript includes many powerful features:

**Destructuring**:
```javascript
const user = { id: 1, username: "jane", role: "OFFICER" };

// Old way
const username = user.username;
const role = user.role;

// Modern way
const { username, role } = user;
```

**Spread Operator**:
```javascript
const user = { username: "jane", role: "OFFICER" };
const updatedUser = { ...user, fullName: "Jane Doe" };
// Result: { username: "jane", role: "OFFICER", fullName: "Jane Doe" }
```

**Template Literals**:
```javascript
const name = "Jane";
const greeting = `Hello, ${name}! Welcome to NISIRCOP.`;
```

---

## Chapter 7: Vue.js Framework

### 7.1 What is Vue.js?

**Vue.js** is a progressive JavaScript framework for building user interfaces. It focuses on the view layer and is easy to integrate.

**Key Features**:
- **Reactive**: Data changes automatically update the UI
- **Component-Based**: Build reusable UI components
- **Easy to Learn**: Gentle learning curve
- **Performant**: Virtual DOM for efficient updates

### 7.2 Vue Components

Components are reusable pieces of UI with their own logic and styling.

**Single File Component** (`.vue` file):
```vue
<template>
  <!-- HTML Template -->
  <div class="user-card">
    <h2>{{ username }}</h2>
    <p>Role: {{ role }}</p>
    <button @click="handleClick">View Profile</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Reactive data
const username = ref('officer_jane');
const role = ref('OFFICER');

// Methods
function handleClick() {
    console.log('Button clicked!');
}
</script>

<style scoped>
.user-card {
    border: 1px solid #ccc;
    padding: 1rem;
    border-radius: 8px;
}
</style>
```

**Three Parts**:
1. **Template**: HTML structure with Vue directives
2. **Script**: JavaScript logic and data
3. **Style**: CSS styling (scoped to this component)

### 7.3 Composition API

Vue 3 introduced the **Composition API** for better code organization.

**Reactive State**:
```vue
<script setup>
import { ref, reactive, computed } from 'vue';

// ref - for primitive values
const count = ref(0);
const username = ref('');

// reactive - for objects
const user = reactive({
    id: 1,
    username: 'jane',
    role: 'OFFICER'
});

// computed - derived values
const displayName = computed(() => {
    return `Officer ${user.username}`;
});

// Update values
function increment() {
    count.value++;  // Access ref values with .value
}
</script>
```

### 7.4 Vue Directives

Directives are special attributes that provide functionality:

**v-if / v-else** (Conditional Rendering):
```vue
<template>
  <div v-if="isLoggedIn">
    Welcome back, {{ username }}!
  </div>
  <div v-else>
    Please log in.
  </div>
</template>
```

**v-for** (List Rendering):
```vue
<template>
  <ul>
    <li v-for="user in users" :key="user.id">
      {{ user.username }} - {{ user.role }}
    </li>
  </ul>
</template>
```

**v-model** (Two-Way Binding):
```vue
<template>
  <input v-model="username" placeholder="Enter username">
  <p>You typed: {{ username }}</p>
</template>

<script setup>
import { ref } from 'vue';
const username = ref('');
</script>
```

**@click** (Event Handling):
```vue
<template>
  <button @click="handleSubmit">Submit</button>
</template>

<script setup>
function handleSubmit() {
    console.log('Form submitted!');
}
</script>
```

### 7.5 Pinia: State Management

**Pinia** is Vue's state management library for sharing data across components.

**Define a Store**:
```javascript
// stores/user.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useUserStore = defineStore('user', () => {
    // State
    const currentUser = ref(null);
    const token = ref(null);
    
    // Getters
    const isAuthenticated = computed(() => token.value !== null);
    const userRole = computed(() => currentUser.value?.role);
    
    // Actions
    async function login(username, password) {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
        const data = await response.json();
        currentUser.value = data.user;
        token.value = data.token;
    }
    
    function logout() {
        currentUser.value = null;
        token.value = null;
    }
    
    return { currentUser, token, isAuthenticated, userRole, login, logout };
});
```

**Use in Component**:
```vue
<script setup>
import { useUserStore } from '@/stores/user';

const userStore = useUserStore();

// Access state
console.log(userStore.currentUser);
console.log(userStore.isAuthenticated);

// Call actions
function handleLogin() {
    userStore.login('officer_jane', 'password123');
}
</script>
```

### 7.6 Vite: Build Tool

**Vite** is a modern build tool that provides:
- **Fast Development Server**: Instant hot module replacement
- **Optimized Builds**: Production bundles with code splitting
- **ES Modules**: Native ESM support

**Vite Configuration** (`vite.config.js`):
```javascript
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
    plugins: [vue()],
    server: {
        port: 3000,
        proxy: {
            '/api': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    }
});
```

---

## Chapter 8: Tailwind CSS

### 8.1 What is Tailwind CSS?

**Tailwind CSS** is a utility-first CSS framework. Instead of pre-designed components, you compose designs using utility classes.

**Traditional CSS**:
```css
.button {
    background-color: blue;
    color: white;
    padding: 0.5rem 1rem;
    border-radius: 0.25rem;
}
```
```html
<button class="button">Click Me</button>
```

**Tailwind Approach**:
```html
<button class="bg-blue-500 text-white px-4 py-2 rounded">
    Click Me
</button>
```

### 8.2 Common Tailwind Utilities

**Layout**:
```html
<div class="flex items-center justify-between">
    <!-- Flexbox with centered items and space between -->
</div>

<div class="grid grid-cols-3 gap-4">
    <!-- 3-column grid with gap -->
</div>
```

**Spacing**:
```html
<div class="p-4">Padding: 1rem</div>
<div class="m-8">Margin: 2rem</div>
<div class="px-4 py-2">Padding X: 1rem, Y: 0.5rem</div>
```

**Colors**:
```html
<div class="bg-blue-500 text-white">Blue background, white text</div>
<div class="hover:bg-blue-700">Darker on hover</div>
```

**Typography**:
```html
<h1 class="text-4xl font-bold">Large, bold heading</h1>
<p class="text-gray-600">Gray paragraph</p>
```

**Responsive Design**:
```html
<div class="w-full md:w-1/2 lg:w-1/3">
    <!-- Full width on mobile, half on tablet, third on desktop -->
</div>
```

---

# Part IV: Database & Geospatial Concepts

## Chapter 9: PostgreSQL Database

### 9.1 What is a Database?

A **database** is an organized collection of structured data. Think of it as a digital filing cabinet.

**Relational Databases** (like PostgreSQL) organize data in **tables**:
```
Users Table:
┌────┬──────────────┬──────────┬───────────┬────────────┐
│ id │   username   │ password │   role    │ station_id │
├────┼──────────────┼──────────┼───────────┼────────────┤
│  1 │ super_user   │ *******  │SUPER_USER │    NULL    │
│  2 │ officer_jane │ *******  │  OFFICER  │      1     │
│  3 │ officer_john │ *******  │  OFFICER  │      1     │
└────┴──────────────┴──────────┴───────────┴────────────┘
```

### 9.2 SQL: Structured Query Language

**SQL** is the language for interacting with databases.

**Create Table**:
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100),
    station_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Insert Data**:
```sql
INSERT INTO users (username, password, role, full_name, station_id)
VALUES ('officer_jane', '$2a$10$...', 'OFFICER', 'Jane Doe', 1);
```

**Query Data**:
```sql
-- Get all officers
SELECT * FROM users WHERE role = 'OFFICER';

-- Get users from station 1
SELECT * FROM users WHERE station_id = 1;

-- Count users by role
SELECT role, COUNT(*) as count
FROM users
GROUP BY role;
```

**Update Data**:
```sql
UPDATE users
SET full_name = 'Jane Smith'
WHERE id = 2;
```

**Delete Data**:
```sql
DELETE FROM users WHERE id = 3;
```

### 9.3 Relationships Between Tables

Tables can be related to each other:

**One-to-Many Relationship**:
```sql
-- One station has many users
CREATE TABLE boundaries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    station_id INT REFERENCES boundaries(id)  -- Foreign Key
);
```

**Join Queries**:
```sql
-- Get users with their station names
SELECT u.username, u.role, b.name as station_name
FROM users u
JOIN boundaries b ON u.station_id = b.id;
```

### 9.4 Indexes for Performance

**Indexes** speed up queries by creating a lookup structure:

```sql
-- Without index: Database scans entire table
-- With index: Database uses optimized lookup

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
```

**Think of an index like a book's table of contents** - instead of reading every page to find a topic, you check the index first.

---

## Chapter 10: PostGIS & Geospatial Data

### 10.1 What is Geospatial Data?

**Geospatial data** represents locations and shapes on Earth's surface.

**Types of Geometric Data**:
- **Point**: A single location (latitude, longitude)
- **LineString**: A series of connected points (roads, routes)
- **Polygon**: An enclosed area (police station boundaries, districts)

### 10.2 PostGIS Extension

**PostGIS** adds spatial capabilities to PostgreSQL.

**Enable PostGIS**:
```sql
CREATE EXTENSION IF NOT EXISTS postgis;
```

**Store Geospatial Data**:
```sql
CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    location GEOMETRY(Point, 4326) NOT NULL  -- Point with SRID 4326
);

CREATE TABLE boundaries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL  -- Polygon boundary
);
```

**SRID 4326**: Spatial Reference System for GPS coordinates (latitude/longitude)

### 10.3 Spatial Queries

**Insert a Point**:
```sql
INSERT INTO incidents (title, priority, location)
VALUES (
    'Robbery at Main St',
    'HIGH',
    ST_SetSRID(ST_MakePoint(-73.95, 40.75), 4326)  -- longitude, latitude
);
```

**Insert a Polygon** (Boundary):
```sql
INSERT INTO boundaries (name, geom)
VALUES (
    'Central Station Zone',
    ST_GeomFromText('POLYGON((
        -74.0 40.7,
        -74.0 40.8,
        -73.9 40.8,
        -73.9 40.7,
        -74.0 40.7
    ))', 4326)
);
```

**Spatial Queries**:

```sql
-- Is this point inside a boundary?
SELECT ST_Contains(
    (SELECT geom FROM boundaries WHERE id = 1),
    ST_SetSRID(ST_MakePoint(-73.95, 40.75), 4326)
);

-- Find all incidents within a boundary
SELECT i.*
FROM incidents i, boundaries b
WHERE b.id = 1
  AND ST_Contains(b.geom, i.location);

-- Distance between two points (in meters)
SELECT ST_Distance(
    ST_SetSRID(ST_MakePoint(-73.95, 40.75), 4326)::geography,
    ST_SetSRID(ST_MakePoint(-73.98, 40.76), 4326)::geography
);
```

### 10.4 Spatial Indexes

Spatial queries can be slow without indexes:

```sql
-- Create spatial index
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);
CREATE INDEX boundaries_geom_idx ON boundaries USING GIST (geom);
```

**GIST (Generalized Search Tree)**: Optimized for spatial data queries.

---

# Part V: Architecture & Design Patterns

## Chapter 11: Microservices Architecture

### 11.1 NISIRCOP Microservices

The NISIRCOP system is composed of seven backend microservices:

```
                    ┌───────────────┐
                    │    Eureka     │
                    │    Server     │
                    │   (8761)      │
                    └───────┬───────┘
                            │
                ┌───────────┴──────────────┐
                │                          │
        ┌───────▼──────┐          ┌───────▼──────┐
        │  API Gateway │          │  PostgreSQL  │
        │   (8080)     │          │  Database    │
        └──────┬───────┘          └───────┬──────┘
               │                          │
    ┌──────────┼──────────┬───────────────┼───────────┬───────────┐
    │          │          │               │           │           │
┌───▼────┐ ┌──▼─────┐ ┌──▼────────┐ ┌────▼─────┐ ┌──▼─────┐ ┌───▼──────┐
│  Auth  │ │  User  │ │ Incident  │ │Geographic│ │Analytics│ │ Frontend │
│Service │ │Service │ │  Service  │ │ Service  │ │ Service │ │  (3000)  │
│ (8081) │ │ (8082) │ │  (8083)   │ │  (8084)  │ │ (8085)  │ │          │
└────────┘ └────────┘ └───────────┘ └──────────┘ └─────────┘ └──────────┘
```

### 11.2 Service Responsibilities

**1. Eureka Server** (Port 8761)
- **Purpose**: Service registry and discovery
- **Responsibility**: Track all microservice instances
- **Used by**: All other services

**2. API Gateway** (Port 8080)
- **Purpose**: Single entry point for clients
- **Responsibilities**:
  - Route requests to appropriate services
  - Load balancing
  - Authentication/authorization
  - Rate limiting
- **Routes**:
  - `/api/v1/auth/**` → Auth Service
  - `/api/v1/users/**` → User Service
  - `/api/v1/incidents/**` → Incident Service
  - `/api/v1/geo/**` → Geographic Service
  - `/api/v1/analytics/**` → Analytics Service

**3. Auth Service** (Port 8081)
- **Purpose**: User authentication
- **Responsibilities**:
  - Validate credentials
  - Generate JWT tokens
  - Token validation
- **Endpoints**:
  - `POST /auth/login` - User login
  - `POST /auth/validate` - Validate token

**4. User Service** (Port 8082)
- **Purpose**: User management
- **Responsibilities**:
  - CRUD operations for users
  - Role management
  - User profile data
- **Endpoints**:
  - `GET /api/v1/users` - List users
  - `POST /api/v1/users` - Create user
  - `GET /api/v1/users/{id}` - Get user details
  - `PUT /api/v1/users/{id}` - Update user
  - `DELETE /api/v1/users/{id}` - Delete user

**5. Incident Service** (Port 8083)
- **Purpose**: Crime incident management
- **Responsibilities**:
  - Report new incidents
  - Query incidents
  - Update incident status
- **Endpoints**:
  - `POST /api/v1/incidents` - Report incident
  - `GET /api/v1/incidents` - List incidents
  - `GET /api/v1/incidents/{id}` - Get incident details

**6. Geographic Service** (Port 8084)
- **Purpose**: Geospatial operations
- **Responsibilities**:
  - Boundary validation
  - Spatial queries
  - Location-based filtering
- **Endpoints**:
  - `POST /api/v1/geo/validate` - Validate point in boundary
  - `GET /api/v1/geo/boundaries` - Get boundaries

**7. Analytics Service** (Port 8085)
- **Purpose**: Crime analytics and reporting
- **Responsibilities**:
  - Aggregate incident data
  - Generate trends
  - Create visualizations data
- **Endpoints**:
  - `GET /api/v1/analytics/trends` - Crime trends
  - `GET /api/v1/analytics/heatmap` - Heat map data

### 11.3 Communication Patterns

**Synchronous Communication** (REST):
```
User Service → Geographic Service
[Verify user's station boundary exists]

Incident Service → Geographic Service
[Validate incident location is within boundaries]

Analytics Service → Incident Service
[Query incident data for analysis]
```

**Example Flow: Report Incident**:
```
1. Client → API Gateway
   POST /api/v1/incidents { title, location, priority }

2. API Gateway → Auth Service
   Validate JWT token

3. API Gateway → Incident Service
   Forward request with user info

4. Incident Service → Geographic Service
   Validate location is within boundaries

5. Incident Service → Database
   Save incident

6. Database → Incident Service
   Return saved incident

7. Incident Service → API Gateway → Client
   Return response
```

### 11.4 Microfrontends

The frontend is split into independent applications:

```
┌─────────────────────────────────────────┐
│           Shell Application             │
│  (Main container, navigation, layout)   │
│                                         │
│  ┌───────────┐ ┌──────────┐ ┌────────┐ │
│  │ Incidents │ │  Users   │ │Analytics│ │
│  │  Module   │ │  Module  │ │ Module  │ │
│  └───────────┘ └──────────┘ └────────┘ │
└─────────────────────────────────────────┘
```

**Benefits**:
- Independent development
- Technology flexibility
- Isolated deployments
- Team autonomy

---

## Chapter 12: Security & Authentication

### 12.1 JWT (JSON Web Tokens)

**JWT** is a standard for securely transmitting information between parties as a JSON object.

**Structure**: `header.payload.signature`

**Example JWT**:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJ1c2VyaWQiOjEsInVzZXJuYW1lIjoib2ZmaWNlcl9qYW5lIiwicm9sZSI6Ik9GRklDRVIifQ.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Decoded Payload**:
```json
{
  "userid": 1,
  "username": "officer_jane",
  "role": "OFFICER",
  "exp": 1672531200
}
```

### 12.2 Authentication Flow

```
1. User Login
   Client → Auth Service
   POST /auth/login { username, password }

2. Validate Credentials
   Auth Service → Database
   Query user by username
   Compare hashed passwords

3. Generate Token
   Auth Service creates JWT with user info
   Signs with secret key

4. Return Token
   Auth Service → Client
   { token: "eyJhbG...", user: {...} }

5. Authenticated Requests
   Client → API Gateway
   GET /api/v1/incidents
   Header: Authorization: Bearer eyJhbG...

6. Validate Token
   API Gateway → Auth Service
   POST /auth/validate { token }

7. Forward Request
   API Gateway → Incident Service
   Include user info from token
```

### 12.3 Role-Based Access Control (RBAC)

**Roles in NISIRCOP**:
- **OFFICER**: Basic level, can report incidents
- **POLICE_STATION**: Station administrator, can manage officers
- **SUPER_USER**: System administrator, full access

**Authorization Example**:
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @GetMapping
    @PreAuthorize("hasRole('SUPER_USER')")  // Only SUPER_USER
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_USER', 'POLICE_STATION')")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
}
```

### 12.4 Password Security

**Never store plain passwords!**

**BCrypt Hashing**:
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

// Hash password
String plainPassword = "password123";
String hashedPassword = encoder.encode(plainPassword);
// Result: $2a$10$e.ExV8sY.s/5/DaJ4WYRz.oO/vBE3g0fAC5.WfXQ.Lz/jd.3r/J7a

// Verify password
boolean matches = encoder.matches("password123", hashedPassword);
// Result: true
```

**Why BCrypt?**
- **Salting**: Each password gets unique salt
- **Adaptive**: Can increase difficulty over time
- **Slow**: Prevents brute-force attacks

---

# Part VI: The NISIRCOP System Deep Dive

## Chapter 13: System Setup & Configuration

### 13.1 Environment Variables

Environment variables configure the application for different environments (development, production).

**`.env` File**:
```env
# Database Configuration
POSTGRES_PASSWORD=your_strong_database_password

# Security Configuration
JWT_SECRET=your_super_secret_jwt_key_that_is_at_least_256_bits_long
```

**Usage in Docker Compose**:
```yaml
services:
  db:
    environment:
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
  
  auth-service:
    environment:
      - JWT_SECRET=${JWT_SECRET}
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/nisircop
```

### 13.2 Application Configuration

**`application.properties` (Spring Boot)**:
```properties
# Application name
spring.application.name=user-service

# Server port
server.port=8082

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/nisircop
spring.datasource.username=postgres
spring.datasource.password=${POSTGRES_PASSWORD}

# JPA configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Eureka configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
eureka.instance.prefer-ip-address=true
```

### 13.3 Dockerfile

**Dockerfile** defines how to build a Docker image.

**Backend Service Dockerfile**:
```dockerfile
# Stage 1: Build
FROM gradle:7.6-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Explanation**:
- **Multi-stage build**: First stage builds, second stage runs
- **FROM**: Base image to use
- **WORKDIR**: Set working directory
- **COPY**: Copy files into image
- **RUN**: Execute commands
- **EXPOSE**: Document which port the container uses
- **ENTRYPOINT**: Command to run when container starts

**Frontend Dockerfile**:
```dockerfile
# Stage 1: Build
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### 13.4 Docker Compose

**Docker Compose** orchestrates multiple containers.

**Key Concepts**:
- **Services**: Each container is a service
- **Networks**: Services can communicate
- **Volumes**: Persistent data storage
- **Dependencies**: Control startup order
- **Health Checks**: Ensure services are ready

**Example Service Definition**:
```yaml
user-service:
  build: ./backend/user-service  # Build from Dockerfile
  container_name: user-service
  ports:
    - "8082:8082"  # Map host:container ports
  depends_on:
    db:
      condition: service_healthy  # Wait for database
    eureka-server:
      condition: service_healthy
  environment:
    - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
    - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/nisircop
    - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka
  networks:
    - nisircop-network
```

---

## Chapter 14: Database Schema Design

### 14.1 NISIRCOP Database Schema

**Entity-Relationship Diagram**:
```
┌──────────────────┐
│     users        │
├──────────────────┤
│ id (PK)          │
│ username         │
│ password         │
│ role             │
│ full_name        │
│ station_id (FK)  │───┐
│ created_at       │   │
└──────────────────┘   │
         │             │
         │ reported_by │
         │             │
         ▼             │
┌──────────────────┐   │
│   incidents      │   │
├──────────────────┤   │
│ id (PK)          │   │
│ title            │   │
│ description      │   │
│ incident_type    │   │
│ priority         │   │
│ location (Point) │   │
│ reported_by (FK) │   │
│ created_at       │   │
└──────────────────┘   │
                       │
                       ▼
               ┌──────────────────┐
               │   boundaries     │
               ├──────────────────┤
               │ id (PK)          │
               │ name             │
               │ geom (Polygon)   │
               └──────────────────┘
```

### 14.2 Table Definitions

**Users Table**:
```sql
CREATE TYPE user_role AS ENUM ('OFFICER', 'POLICE_STATION', 'SUPER_USER');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- BCrypt hashed
    role user_role NOT NULL,
    full_name VARCHAR(100),
    station_id INT REFERENCES boundaries(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_station_id ON users(station_id);
```

**Incidents Table**:
```sql
CREATE TYPE incident_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');

CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    incident_type VARCHAR(100),
    priority incident_priority NOT NULL,
    location GEOMETRY(Point, 4326) NOT NULL,
    reported_by INT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
CREATE INDEX idx_incidents_priority ON incidents(priority);
CREATE INDEX idx_incidents_type ON incidents(incident_type);
```

**Boundaries Table**:
```sql
CREATE TABLE boundaries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL
);

-- Spatial index
CREATE INDEX boundaries_geom_idx ON boundaries USING GIST (geom);
```

### 14.3 Sample Queries

**User Queries**:
```sql
-- Get all officers
SELECT * FROM users WHERE role = 'OFFICER';

-- Get users from a specific station
SELECT u.*, b.name as station_name
FROM users u
JOIN boundaries b ON u.station_id = b.id
WHERE b.name = 'Central Station Zone';
```

**Incident Queries**:
```sql
-- Get high-priority incidents
SELECT * FROM incidents 
WHERE priority IN ('HIGH', 'CRITICAL')
ORDER BY created_at DESC;

-- Get incidents within a boundary
SELECT i.*
FROM incidents i, boundaries b
WHERE b.id = 1 AND ST_Contains(b.geom, i.location);

-- Count incidents by type
SELECT incident_type, COUNT(*) as count
FROM incidents
GROUP BY incident_type
ORDER BY count DESC;
```

**Analytics Queries**:
```sql
-- Daily incident trends
SELECT 
    DATE(created_at) as date,
    COUNT(*) as incident_count
FROM incidents
WHERE created_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY DATE(created_at)
ORDER BY date;

-- Incidents per boundary
SELECT 
    b.name as boundary_name,
    COUNT(i.id) as incident_count
FROM boundaries b
LEFT JOIN incidents i ON ST_Contains(b.geom, i.location)
GROUP BY b.id, b.name;
```

---

## Chapter 15: API Design & Implementation

### 15.1 RESTful API Design Principles

**Resource-Based URLs**:
```
Good:
  GET /api/v1/users/123
  POST /api/v1/incidents

Bad:
  GET /api/v1/getUser?id=123
  POST /api/v1/createIncident
```

**HTTP Status Codes**:
- **200 OK**: Request successful
- **201 Created**: Resource created successfully
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

### 15.2 User Service Implementation

**Model**:
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    private String fullName;
    
    @Column(name = "station_id")
    private Long stationId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters and setters...
}

public enum Role {
    OFFICER, POLICE_STATION, SUPER_USER
}
```

**Repository**:
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
    List<User> findByStationId(Long stationId);
}
```

**Service**:
```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    public User create(User user) {
        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public User update(Long id, User userDetails) {
        User user = findById(id);
        user.setFullName(userDetails.getFullName());
        user.setRole(userDetails.getRole());
        user.setStationId(userDetails.getStationId());
        return userRepository.save(user);
    }
    
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}
```

**Controller**:
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_USER', 'POLICE_STATION')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.update(id, user));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 15.3 Error Handling

**Custom Exception**:
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**Global Exception Handler**:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
        ResourceNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }
}
```

---

# Part VII: Containerization & Deployment

## Chapter 16: Docker Fundamentals

### 16.1 What is Docker?

**Docker** is a platform for developing, shipping, and running applications in **containers**.

**Container vs. Virtual Machine**:
```
Virtual Machine:
┌─────────────────────────────┐
│      Application            │
├─────────────────────────────┤
│      Guest OS (GB)          │
├─────────────────────────────┤
│      Hypervisor             │
├─────────────────────────────┤
│      Host OS                │
├─────────────────────────────┤
│      Hardware               │
└─────────────────────────────┘

Container:
┌─────────────────────────────┐
│      Application            │
├─────────────────────────────┤
│    Container Runtime        │
├─────────────────────────────┤
│      Host OS                │
├─────────────────────────────┤
│      Hardware               │
└─────────────────────────────┘
```

**Containers are**:
- **Lightweight**: Share host OS kernel
- **Fast**: Start in seconds
- **Portable**: Run anywhere Docker runs
- **Isolated**: Each container is independent

### 16.2 Docker Images

An **image** is a blueprint for containers. It contains:
- Application code
- Runtime environment
- System libraries
- Configuration

**Create Image from Dockerfile**:
```bash
docker build -t user-service:latest ./backend/user-service
```

**List Images**:
```bash
docker images
```

### 16.3 Docker Containers

A **container** is a running instance of an image.

**Run Container**:
```bash
docker run -d -p 8082:8082 --name user-service user-service:latest
```

**Flags**:
- `-d`: Detached mode (run in background)
- `-p 8082:8082`: Map port host:container
- `--name`: Give container a name

**Manage Containers**:
```bash
docker ps                    # List running containers
docker ps -a                 # List all containers
docker stop user-service     # Stop container
docker start user-service    # Start container
docker logs user-service     # View logs
docker exec -it user-service bash  # Access container shell
```

### 16.4 Docker Networks

Containers can communicate through Docker networks:

```bash
# Create network
docker network create nisircop-network

# Run containers on network
docker run -d --network nisircop-network --name db postgres:15
docker run -d --network nisircop-network --name user-service user-service:latest
```

**Containers on the same network can communicate using container names**:
```
jdbc:postgresql://db:5432/nisircop
                    ↑
                Container name as hostname
```

### 16.5 Docker Volumes

**Volumes** provide persistent data storage:

```bash
# Create volume
docker volume create nisircop-data

# Use volume
docker run -d -v nisircop-data:/var/lib/postgresql/data postgres:15
```

**Data in volumes persists even after container is deleted**.

---

## Chapter 17: Docker Compose

### 17.1 What is Docker Compose?

**Docker Compose** simplifies running multi-container applications.

**Without Compose** (many commands):
```bash
docker network create nisircop-network
docker run -d --network nisircop-network --name db ...
docker run -d --network nisircop-network --name eureka ...
docker run -d --network nisircop-network --name user-service ...
# ... many more commands
```

**With Compose** (one command):
```bash
docker-compose up
```

### 17.2 Docker Compose File

**`docker-compose.yml`**:
```yaml
version: '3.8'

services:
  # PostgreSQL Database
  db:
    image: postgis/postgis:15-3.3
    container_name: nisircop_db
    environment:
      POSTGRES_DB: nisircop
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - nisircop-data:/var/lib/postgresql/data
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d nisircop"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Eureka Server
  eureka-server:
    build: ./backend/eureka-server
    container_name: eureka-server
    ports:
      - "8761:8761"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8761"]
      interval: 30s
      timeout: 10s
      retries: 5

  # User Service
  user-service:
    build: ./backend/user-service
    container_name: user-service
    depends_on:
      db:
        condition: service_healthy
      eureka-server:
        condition: service_healthy
    environment:
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/nisircop
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-server:8761/eureka

volumes:
  nisircop-data:
```

### 17.3 Docker Compose Commands

```bash
# Start all services
docker-compose up

# Start in detached mode
docker-compose up -d

# Build and start
docker-compose up --build

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# View logs
docker-compose logs

# View logs for specific service
docker-compose logs user-service

# Scale a service
docker-compose up --scale user-service=3
```

### 17.4 Health Checks

Health checks ensure services are ready before dependent services start:

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U postgres -d nisircop"]
  interval: 10s      # Check every 10 seconds
  timeout: 5s        # Timeout after 5 seconds
  retries: 5         # Retry 5 times before marking unhealthy
```

**Dependent Service**:
```yaml
user-service:
  depends_on:
    db:
      condition: service_healthy  # Wait for db to be healthy
```

---

## Chapter 18: Deployment Workflow

### 18.1 Local Development Setup

**Prerequisites**:
- Docker Desktop (includes Docker and Docker Compose)
- Git

**Steps**:
```bash
# 1. Clone repository
git clone https://github.com/your-org/nisircop.git
cd nisircop

# 2. Create environment file
cat > .env << EOF
POSTGRES_PASSWORD=your_strong_password
JWT_SECRET=your_super_secret_jwt_key_256_bits_long
EOF

# 3. Start application
docker-compose up --build -d

# 4. Wait for services to start (check logs)
docker-compose logs -f

# 5. Access application
# Frontend: http://localhost:3000
# API Gateway: http://localhost:8080
# Eureka: http://localhost:8761
```

### 18.2 Monitoring & Debugging

**Check Service Status**:
```bash
docker-compose ps
```

**View Logs**:
```bash
# All services
docker-compose logs

# Specific service
docker-compose logs user-service

# Follow logs (live)
docker-compose logs -f user-service

# Last 100 lines
docker-compose logs --tail=100 user-service
```

**Access Container**:
```bash
docker-compose exec user-service bash
```

**Check Database**:
```bash
docker-compose exec db psql -U postgres -d nisircop

# Inside PostgreSQL
\dt              # List tables
\d users         # Describe users table
SELECT * FROM users;
```

### 18.3 Troubleshooting

**Service Won't Start**:
```bash
# Check logs
docker-compose logs service-name

# Common issues:
# - Port already in use
# - Missing environment variables
# - Database not ready
# - Build errors
```

**Database Connection Errors**:
```bash
# Verify database is running
docker-compose ps db

# Check database logs
docker-compose logs db

# Test connection
docker-compose exec db pg_isready -U postgres
```

**Clear Everything and Restart**:
```bash
# Stop and remove everything
docker-compose down -v

# Remove images
docker-compose down --rmi all

# Rebuild and start
docker-compose up --build
```

---

# Part VIII: Hands-On Exercises

## Chapter 19: Building Your First Feature

### Exercise 1: Add Incident Status Field

**Objective**: Add a status field to incidents (OPEN, INVESTIGATING, CLOSED)

**Steps**:

1. **Update Database Schema**:
```sql
-- Add enum type
CREATE TYPE incident_status AS ENUM ('OPEN', 'INVESTIGATING', 'CLOSED');

-- Add column
ALTER TABLE incidents ADD COLUMN status incident_status DEFAULT 'OPEN';
```

2. **Update Java Model**:
```java
public enum IncidentStatus {
    OPEN, INVESTIGATING, CLOSED
}

@Entity
@Table(name = "incidents")
public class Incident {
    // ... existing fields
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentStatus status = IncidentStatus.OPEN;
    
    // Getter and setter
    public IncidentStatus getStatus() {
        return status;
    }
    
    public void setStatus(IncidentStatus status) {
        this.status = status;
    }
}
```

3. **Add Update Endpoint**:
```java
@PutMapping("/{id}/status")
public ResponseEntity<Incident> updateStatus(
    @PathVariable Long id,
    @RequestBody Map<String, String> request
) {
    Incident incident = incidentService.findById(id);
    String statusStr = request.get("status");
    IncidentStatus status = IncidentStatus.valueOf(statusStr);
    incident.setStatus(status);
    return ResponseEntity.ok(incidentService.save(incident));
}
```

4. **Test**:
```bash
# Update status
curl -X PUT http://localhost:8080/api/v1/incidents/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "INVESTIGATING"}'
```

### Exercise 2: Add Incident Comments

**Objective**: Allow users to add comments to incidents

**Steps**:

1. **Create Comments Table**:
```sql
CREATE TABLE incident_comments (
    id SERIAL PRIMARY KEY,
    incident_id INT REFERENCES incidents(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id),
    comment TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_incident_id ON incident_comments(incident_id);
```

2. **Create Comment Model**:
```java
@Entity
@Table(name = "incident_comments")
public class IncidentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "incident_id", nullable = false)
    private Long incidentId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String comment;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters and setters...
}
```

3. **Create Repository**:
```java
public interface IncidentCommentRepository 
    extends JpaRepository<IncidentComment, Long> {
    
    List<IncidentComment> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}
```

4. **Add Endpoints**:
```java
@PostMapping("/{incidentId}/comments")
public ResponseEntity<IncidentComment> addComment(
    @PathVariable Long incidentId,
    @RequestBody Map<String, String> request,
    @AuthenticationPrincipal User currentUser
) {
    IncidentComment comment = new IncidentComment();
    comment.setIncidentId(incidentId);
    comment.setUserId(currentUser.getId());
    comment.setComment(request.get("comment"));
    comment.setCreatedAt(LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentRepository.save(comment));
}

@GetMapping("/{incidentId}/comments")
public ResponseEntity<List<IncidentComment>> getComments(
    @PathVariable Long incidentId
) {
    return ResponseEntity.ok(
        commentRepository.findByIncidentIdOrderByCreatedAtDesc(incidentId)
    );
}
```

### Exercise 3: Create Dashboard Widget

**Objective**: Build a Vue component showing incident statistics

**Create Component** (`IncidentStats.vue`):
```vue
<template>
  <div class="stats-grid">
    <div class="stat-card">
      <h3>Total Incidents</h3>
      <p class="stat-value">{{ stats.total }}</p>
    </div>
    
    <div class="stat-card critical">
      <h3>Critical</h3>
      <p class="stat-value">{{ stats.critical }}</p>
    </div>
    
    <div class="stat-card high">
      <h3>High Priority</h3>
      <p class="stat-value">{{ stats.high }}</p>
    </div>
    
    <div class="stat-card open">
      <h3>Open</h3>
      <p class="stat-value">{{ stats.open }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const stats = ref({
  total: 0,
  critical: 0,
  high: 0,
  open: 0
});

async function loadStats() {
  try {
    const response = await fetch('/api/v1/analytics/stats');
    stats.value = await response.json();
  } catch (error) {
    console.error('Failed to load stats:', error);
  }
}

onMounted(() => {
  loadStats();
});
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  padding: 1rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.stat-value {
  font-size: 2.5rem;
  font-weight: bold;
  color: #2563eb;
  margin: 0;
}

.stat-card.critical .stat-value {
  color: #dc2626;
}

.stat-card.high .stat-value {
  color: #f59e0b;
}

.stat-card.open .stat-value {
  color: #10b981;
}
</style>
```

---

## Chapter 20: Testing Practices

### 20.1 Unit Testing (Backend)

**JUnit Test Example**:
```java
@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testCreateUser() {
        // Arrange
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("password123");
        user.setRole(Role.OFFICER);
        
        when(userRepository.save(any(User.class)))
            .thenReturn(user);
        
        // Act
        User created = userService.create(user);
        
        // Assert
        assertNotNull(created);
        assertEquals("test_user", created.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    public void testFindById_NotFound() {
        // Arrange
        when(userRepository.findById(999L))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(999L);
        });
    }
}
```

### 20.2 Integration Testing

**Test REST Endpoints**:
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @WithMockUser(roles = "SUPER_USER")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    @WithMockUser(roles = "SUPER_USER")
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("new_user");
        user.setPassword("password123");
        user.setRole(Role.OFFICER);
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("new_user"));
    }
}
```

### 20.3 Frontend Testing

**Vitest Test Example**:
```javascript
import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import IncidentStats from '@/components/IncidentStats.vue';

describe('IncidentStats', () => {
  it('renders stats correctly', () => {
    const wrapper = mount(IncidentStats, {
      props: {
        stats: {
          total: 100,
          critical: 5,
          high: 15,
          open: 75
        }
      }
    });
    
    expect(wrapper.text()).toContain('100');
    expect(wrapper.text()).toContain('5');
  });
  
  it('applies correct CSS classes', () => {
    const wrapper = mount(IncidentStats);
    
    expect(wrapper.find('.stat-card.critical').exists()).toBe(true);
    expect(wrapper.find('.stat-card.high').exists()).toBe(true);
  });
});
```

---

# Part IX: Advanced Topics

## Chapter 21: Performance Optimization

### 21.1 Database Optimization

**Use Indexes Wisely**:
```sql
-- Analyze query performance
EXPLAIN ANALYZE
SELECT * FROM incidents
WHERE created_at > '2024-01-01'
  AND priority = 'HIGH';

-- Add index if needed
CREATE INDEX idx_incidents_created_priority 
ON incidents(created_at, priority);
```

**Connection Pooling**:
```properties
# application.properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

**Pagination for Large Datasets**:
```java
@GetMapping
public ResponseEntity<Page<Incident>> getIncidents(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(incidentService.findAll(pageable));
}
```

### 21.2 Caching

**Spring Cache**:
```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

**Configuration**:
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "incidents");
    }
}
```

### 21.3 Frontend Optimization

**Lazy Loading Components**:
```javascript
// Instead of: import AnalyticsModule from './AnalyticsModule.vue'
const AnalyticsModule = defineAsyncComponent(() =>
  import('./AnalyticsModule.vue')
);
```

**Debounce Search**:
```vue
<script setup>
import { ref, watch } from 'vue';

const searchQuery = ref('');
const results = ref([]);

let debounceTimer;

watch(searchQuery, (newValue) => {
  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(async () => {
    results.value = await searchIncidents(newValue);
  }, 300);  // Wait 300ms after user stops typing
});
</script>
```

---

## Chapter 22: Security Best Practices

### 22.1 Input Validation

**Backend Validation**:
```java
@Entity
public class User {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @Email(message = "Invalid email format")
    private String email;
}
```

**Controller Validation**:
```java
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    // @Valid triggers validation
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.create(user));
}
```

### 22.2 SQL Injection Prevention

**Never Build SQL Manually**:
```java
// BAD - Vulnerable to SQL injection
String sql = "SELECT * FROM users WHERE username = '" + username + "'";

// GOOD - Use parameterized queries
@Query("SELECT u FROM User u WHERE u.username = :username")
User findByUsername(@Param("username") String username);
```

### 22.3 XSS Prevention

**Frontend**: Sanitize User Input
```vue
<script setup>
import DOMPurify from 'dompurify';

function sanitizeHtml(dirty) {
  return DOMPurify.sanitize(dirty);
}
</script>
```

**Backend**: Escape Output
```java
import org.apache.commons.text.StringEscapeUtils;

String safe = StringEscapeUtils.escapeHtml4(userInput);
```

### 22.4 CORS Configuration

```java
@Configuration
public class WebConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = 
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
```

---

## Chapter 23: Scaling & Production Readiness

### 23.1 Horizontal Scaling

Run multiple instances of services:
```yaml
# docker-compose.yml
services:
  user-service:
    build: ./backend/user-service
    deploy:
      replicas: 3  # Run 3 instances
```

**Load Balancing**: API Gateway automatically distributes requests across instances.

### 23.2 Monitoring

**Application Metrics** (Spring Boot Actuator):
```properties
# application.properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=always
```

**Access Metrics**:
```
GET /actuator/health
GET /actuator/metrics
GET /actuator/metrics/jvm.memory.used
```

### 23.3 Logging

**Structured Logging**:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IncidentService {
    
    private static final Logger logger = 
        LoggerFactory.getLogger(IncidentService.class);
    
    public Incident create(Incident incident) {
        logger.info("Creating incident: {}", incident.getTitle());
        try {
            Incident saved = incidentRepository.save(incident);
            logger.info("Incident created with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Failed to create incident", e);
            throw e;
        }
    }
}
```

### 23.4 Environment-Specific Configuration

**Multiple Profiles**:
```
application.properties           # Common config
application-dev.properties       # Development
application-prod.properties      # Production
```

**Activate Profile**:
```bash
# Development
java -jar app.jar --spring.profiles.active=dev

# Production
java -jar app.jar --spring.profiles.active=prod
```

---

## Chapter 24: Future Enhancements

### 24.1 Real-Time Features

**WebSockets for Live Updates**:
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig 
    implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
}
```

### 24.2 Mobile Application

**Considerations**:
- Use same REST API
- JWT authentication works identically
- Consider React Native or Flutter
- Optimize for mobile networks

### 24.3 Advanced Analytics

**Machine Learning Integration**:
- Crime prediction models
- Pattern recognition
- Anomaly detection
- Resource allocation optimization

**Tools**:
- Python with scikit-learn
- TensorFlow
- Apache Spark for big data

### 24.4 Kubernetes Deployment

**Production-Grade Orchestration**:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: nisircop/user-service:latest
        ports:
        - containerPort: 8082
```

---

## Conclusion

You've completed a comprehensive journey through modern full-stack web development! You've learned:

✅ **Fundamentals**: Web development basics, HTTP, APIs, JSON
✅ **Backend**: Java, Spring Boot, Spring Cloud, microservices
✅ **Frontend**: JavaScript, Vue.js, Pinia, Tailwind CSS
✅ **Database**: PostgreSQL, PostGIS, geospatial data
✅ **Architecture**: Microservices, microfrontends, design patterns
✅ **Security**: JWT, RBAC, authentication, best practices
✅ **DevOps**: Docker, Docker Compose, deployment
✅ **Advanced**: Performance, testing, scaling, monitoring

### Next Steps

1. **Practice**: Build features, experiment, break things, fix them
2. **Explore**: Dive deeper into topics that interest you
3. **Contribute**: Join open-source projects
4. **Stay Current**: Technologies evolve - keep learning!

### Additional Resources

**Books**:
- "Spring in Action" by Craig Walls
- "Vue.js: Up and Running" by Callum Macrae
- "Designing Data-Intensive Applications" by Martin Kleppmann

**Online**:
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Vue.js Documentation: https://vuejs.org/
- PostgreSQL Documentation: https://www.postgresql.org/docs/
- PostGIS Documentation: https://postgis.net/documentation/

**Practice**:
- LeetCode for algorithms
- GitHub for reading real-world code
- Build your own projects!

---

**Happy Coding! 🚀**

*Remember: Every expert was once a beginner. Keep learning, keep building, and never stop asking questions.*
