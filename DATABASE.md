# NISIRCOP-LE Database Documentation

Complete guide to understanding, setting up, and working with the NISIRCOP-LE PostgreSQL + PostGIS database.

---

## Table of Contents

1. [Overview](#overview)
2. [Database Setup](#database-setup)
3. [Schema Design](#schema-design)
4. [Working with Spatial Data](#working-with-spatial-data)
5. [Common Database Operations](#common-database-operations)
6. [Database Management](#database-management)
7. [Backup and Restore](#backup-and-restore)
8. [Performance Optimization](#performance-optimization)
9. [Troubleshooting](#troubleshooting)

---

## Overview

### Technology Stack

- **Database**: PostgreSQL 17.6 ✅ **INSTALLED AND RUNNING**
- **Spatial Extension**: PostGIS 3.5.2 ✅ **ENABLED**
- **Coordinate System**: WGS 84 (SRID 4326)
- **Character Encoding**: UTF-8
- **Connection Pool**: HikariCP (via Spring Boot)

### Current Status

✅ **FULLY OPERATIONAL**:
- Database `nisircop` created and initialized
- PostGIS extension enabled
- All tables created (users, incidents, boundaries)
- Spatial indexes created and optimized
- Sample data loaded (7 users, 3 incidents, 1 boundary)
- All microservices connected and tested

### Database Architecture

```
nisircop (Database)
├── PostGIS Extension (for spatial operations)
├── Tables
│   ├── users (authentication & hierarchy)
│   ├── boundaries (police station jurisdictions)
│   └── incidents (crime reports with GPS)
├── Spatial Indexes (GIST)
├── Enums
│   ├── user_role
│   └── incident_priority
└── Sample Data (for testing)
```

### Key Features

- **Spatial Queries**: Point-in-polygon checks, distance calculations
- **Hierarchical Data**: Parent-child user relationships
- **Role-Based Security**: Database-level role enforcement
- **Automatic Timestamps**: Created/updated timestamps
- **Data Validation**: Check constraints and foreign keys

---

## Database Setup

### Option 1: Using Docker (Recommended)

#### Step 1: Start PostgreSQL with PostGIS

```bash
# Using docker-compose (includes init script)
docker-compose up -d db

# Or standalone Docker container
docker run -d \
  --name nisircop-db \
  -e POSTGRES_DB=nisircop \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=your_secure_password \
  -p 5432:5432 \
  -v $(pwd)/database/init.sql:/docker-entrypoint-initdb.d/init.sql \
  -v nisircop-data:/var/lib/postgresql/data \
  postgis/postgis:15-3.3
```

#### Step 2: Verify Database is Running

```bash
# Check container status
docker ps | grep nisircop-db

# Test connection
docker exec -it nisircop-db psql -U postgres -d nisircop -c "SELECT version();"

# Verify PostGIS extension
docker exec -it nisircop-db psql -U postgres -d nisircop -c "SELECT postgis_version();"
```

### Option 2: Manual Installation (Linux)

#### Step 1: Install PostgreSQL and PostGIS

```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install -y postgresql-15 postgresql-15-postgis-3

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

#### Step 2: Create Database

```bash
# Switch to postgres user
sudo -u postgres psql

# Inside psql:
CREATE DATABASE nisircop;
\c nisircop
CREATE EXTENSION postgis;
\q
```

#### Step 3: Run Initialization Script

```bash
# Execute the init script
sudo -u postgres psql -d nisircop -f database/init.sql

# Or manually copy and paste the SQL commands
```

### Option 3: Cloud Database (AWS RDS, Azure, etc.)

#### Step 1: Create PostgreSQL Instance

1. Choose PostgreSQL version 15+
2. Enable PostGIS extension support
3. Configure security groups (port 5432)
4. Set strong master password

#### Step 2: Enable PostGIS Extension

```sql
-- Connect to your cloud database
CREATE EXTENSION IF NOT EXISTS postgis;
```

#### Step 3: Update Connection String

```properties
# In application.properties
spring.datasource.url=jdbc:postgresql://your-rds-endpoint:5432/nisircop
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Schema Design

### Complete Database Schema

✅ **CURRENTLY IMPLEMENTED AND TESTED**

```sql
-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Define ENUM types (used by database, but services use VARCHAR)
CREATE TYPE user_role AS ENUM ('OFFICER', 'POLICE_STATION', 'SUPER_USER');
CREATE TYPE incident_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');

-- Users table (authentication and hierarchy)
-- ✅ TESTED: User Service fully operational with this table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,              -- Changed to BIGSERIAL for JPA compatibility
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,        -- BCrypt hashed with strength 12
    role VARCHAR(20) NOT NULL,             -- OFFICER, POLICE_STATION, SUPER_USER
    full_name VARCHAR(100),
    station_id INT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index for username lookups
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);

-- Boundaries table (police station jurisdictions with PostGIS)
CREATE TABLE boundaries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    geom GEOMETRY(Polygon, 4326) NOT NULL,  -- WGS 84 coordinate system
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by INT REFERENCES users(id)
);

-- Spatial index on geometry column (critical for performance)
CREATE INDEX boundaries_geom_idx ON boundaries USING GIST (geom);

-- Incidents table (crime reports with GPS location)
-- ✅ PostGIS spatial data ready for Incident Service
CREATE TABLE incidents (
    id BIGSERIAL PRIMARY KEY,                    -- Changed to BIGSERIAL
    title VARCHAR(255) NOT NULL,
    description TEXT,
    incident_type VARCHAR(100),
    priority VARCHAR(20) NOT NULL,               -- LOW, MEDIUM, HIGH, CRITICAL
    location GEOMETRY(Point, 4326) NOT NULL,     -- GPS coordinates (PostGIS)
    reported_by BIGINT REFERENCES users(id),     -- Changed to BIGINT
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    occurred_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) DEFAULT 'REPORTED'
);

-- Spatial index on location (critical for geographic queries)
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);

-- Regular indexes for common queries
CREATE INDEX idx_incidents_reported_by ON incidents(reported_by);
CREATE INDEX idx_incidents_type ON incidents(incident_type);
CREATE INDEX idx_incidents_priority ON incidents(priority);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
```

### Entity Relationships

```
users (1) ─────< (many) incidents
  │                        │
  │                        │
  │                        └─> location (POINT with lat/lng)
  │
  └─> (1) creates (many) boundaries
                          │
                          └─> geom (POLYGON defining jurisdiction)
```

### Data Types Explained

| Column Type | Purpose | Example |
|-------------|---------|---------|
| `SERIAL` | Auto-incrementing integer ID | 1, 2, 3, ... |
| `VARCHAR(n)` | Variable-length text (max n) | "Central Station" |
| `TEXT` | Unlimited length text | Long descriptions |
| `GEOMETRY(Point, 4326)` | GPS coordinates (lat, lng) | POINT(38.7525 9.0192) |
| `GEOMETRY(Polygon, 4326)` | Geographic boundary | POLYGON((...)) |
| `TIMESTAMP WITH TIME ZONE` | Date/time with timezone | 2024-10-14 15:30:00+03 |
| `user_role` | Custom enum type | 'OFFICER', 'POLICE_STATION' |

---

## Working with Spatial Data

### Understanding PostGIS Geometry Types

#### Point (for Incidents)

Represents a single GPS location (latitude, longitude):

```sql
-- Create a point at Addis Ababa city center
ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)

-- Longitude first, then Latitude (PostGIS convention)
-- SRID 4326 = WGS 84 (standard GPS coordinate system)
```

#### Polygon (for Boundaries)

Represents an area/jurisdiction:

```sql
-- Create a rectangular boundary
ST_GeomFromText(
  'POLYGON((38.7 9.0, 38.8 9.0, 38.8 9.1, 38.7 9.1, 38.7 9.0))', 
  4326
)

-- Note: First and last point must be identical (closed polygon)
```

### Common Spatial Operations

#### 1. Point-in-Polygon Check

**Check if an incident location is within a boundary:**

```sql
-- Is this incident within boundary ID 1?
SELECT 
    i.id,
    i.title,
    ST_Within(i.location, b.geom) as is_within
FROM incidents i
CROSS JOIN boundaries b
WHERE b.id = 1;

-- Find all incidents within a specific boundary
SELECT i.*
FROM incidents i
JOIN boundaries b ON ST_Within(i.location, b.geom)
WHERE b.name = 'Central Station Zone';
```

#### 2. Distance Calculations

**Calculate distance between two points:**

```sql
-- Distance in meters between two incidents
SELECT 
    i1.id as incident1_id,
    i2.id as incident2_id,
    ST_Distance(
        i1.location::geography,
        i2.location::geography
    ) as distance_meters
FROM incidents i1, incidents i2
WHERE i1.id = 1 AND i2.id = 2;

-- Find incidents within 500 meters of a point
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
    500  -- 500 meters
);
```

#### 3. Boundary Containment

**Check if one boundary contains another:**

```sql
-- Does boundary 1 contain boundary 2?
SELECT ST_Contains(b1.geom, b2.geom)
FROM boundaries b1, boundaries b2
WHERE b1.id = 1 AND b2.id = 2;
```

#### 4. Find Nearest Features

**Find 5 nearest incidents to a location:**

```sql
SELECT 
    id,
    title,
    ST_Distance(
        location::geography,
        ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)::geography
    ) as distance
FROM incidents
ORDER BY location <-> ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326)
LIMIT 5;
```

#### 5. Extract Coordinates from Geometry

**Get latitude and longitude from a point:**

```sql
SELECT 
    id,
    title,
    ST_Y(location) as latitude,
    ST_X(location) as longitude
FROM incidents;
```

---

## Common Database Operations

### User Management

#### Create Users with Different Roles

```sql
-- Create SUPER_USER (National Administrator)
INSERT INTO users (username, password, role, full_name, station_id)
VALUES (
    'admin_addis',
    '$2a$12$hashed_password_here',  -- Use BCrypt hash from application
    'SUPER_USER',
    'Addis Ababa Regional Commander',
    NULL
);

-- Create POLICE_STATION user
INSERT INTO users (username, password, role, full_name, station_id)
VALUES (
    'station_merkato',
    '$2a$12$hashed_password_here',
    'POLICE_STATION',
    'Merkato Station Commander',
    1
);

-- Create OFFICER user
INSERT INTO users (username, password, role, full_name, station_id)
VALUES (
    'officer_abebe',
    '$2a$12$hashed_password_here',
    'OFFICER',
    'Officer Abebe Bekele',
    1
);
```

#### Query Users by Role

```sql
-- List all officers
SELECT id, username, full_name, station_id
FROM users
WHERE role = 'OFFICER';

-- List users at a specific station
SELECT id, username, role, full_name
FROM users
WHERE station_id = 1;

-- Count users by role
SELECT role, COUNT(*) as total
FROM users
GROUP BY role;
```

#### Update User Password

```sql
-- Update password (hash must be generated by application)
UPDATE users
SET password = '$2a$12$new_hashed_password',
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'officer_abebe';
```

### Boundary Management

#### Create a Boundary (Police Station Jurisdiction)

```sql
-- Create a boundary for Merkato Police Station
INSERT INTO boundaries (name, description, geom, created_by)
VALUES (
    'Merkato Police Station Zone',
    'Jurisdiction covering Merkato market area',
    ST_GeomFromText(
        'POLYGON((
            38.7400 8.9900,
            38.7600 8.9900,
            38.7600 9.0100,
            38.7400 9.0100,
            38.7400 8.9900
        ))',
        4326
    ),
    1  -- Created by user ID 1
);
```

#### Create Complex Polygons

For more complex boundaries, use tools like:
- [geojson.io](http://geojson.io) - Draw polygons visually
- QGIS - Professional GIS software
- Google Earth - Draw and export KML

**Convert GeoJSON to PostGIS:**

```sql
-- If you have GeoJSON coordinates
INSERT INTO boundaries (name, geom)
VALUES (
    'Complex Boundary',
    ST_GeomFromGeoJSON('{
        "type": "Polygon",
        "coordinates": [[[38.7, 9.0], [38.8, 9.0], [38.8, 9.1], [38.7, 9.1], [38.7, 9.0]]]
    }')
);
```

#### Query Boundaries

```sql
-- List all boundaries with their area
SELECT 
    id,
    name,
    ST_Area(geom::geography) / 1000000 as area_sq_km
FROM boundaries;

-- Get boundary as GeoJSON (for mapping)
SELECT 
    id,
    name,
    ST_AsGeoJSON(geom) as geojson
FROM boundaries
WHERE id = 1;

-- Get boundary as WKT (Well-Known Text)
SELECT 
    id,
    name,
    ST_AsText(geom) as wkt
FROM boundaries;
```

### Incident Management

#### Create an Incident

```sql
-- Report a new incident
INSERT INTO incidents (
    title,
    description,
    incident_type,
    priority,
    location,
    reported_by,
    occurred_at
)
VALUES (
    'Armed Robbery at Bole Road',
    'Two suspects with weapons robbed a jewelry shop',
    'ROBBERY',
    'HIGH',
    ST_SetSRID(ST_MakePoint(38.7525, 9.0192), 4326),
    3,  -- Reported by officer ID 3
    '2024-10-14 15:30:00+03'
);
```

#### Query Incidents

```sql
-- Get all incidents with coordinates
SELECT 
    id,
    title,
    incident_type,
    priority,
    ST_Y(location) as latitude,
    ST_X(location) as longitude,
    occurred_at
FROM incidents
ORDER BY occurred_at DESC;

-- Get incidents by priority
SELECT 
    id,
    title,
    priority,
    occurred_at
FROM incidents
WHERE priority IN ('HIGH', 'CRITICAL')
ORDER BY occurred_at DESC;

-- Get incidents within last 7 days
SELECT 
    id,
    title,
    incident_type,
    created_at
FROM incidents
WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '7 days'
ORDER BY created_at DESC;

-- Get incidents by officer
SELECT 
    i.id,
    i.title,
    i.incident_type,
    u.full_name as reported_by_officer
FROM incidents i
JOIN users u ON i.reported_by = u.id
WHERE u.username = 'officer_abebe';
```

#### Advanced Incident Queries

```sql
-- Incidents by type with count
SELECT 
    incident_type,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE priority = 'CRITICAL') as critical_count
FROM incidents
GROUP BY incident_type
ORDER BY total DESC;

-- Incident density by boundary
SELECT 
    b.name as boundary_name,
    COUNT(i.id) as incident_count
FROM boundaries b
LEFT JOIN incidents i ON ST_Within(i.location, b.geom)
GROUP BY b.id, b.name
ORDER BY incident_count DESC;

-- Hourly incident pattern
SELECT 
    EXTRACT(HOUR FROM occurred_at) as hour_of_day,
    COUNT(*) as incident_count
FROM incidents
WHERE occurred_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY hour_of_day
ORDER BY hour_of_day;
```

### Data Validation Queries

```sql
-- Find incidents outside all boundaries (potential errors)
SELECT i.id, i.title
FROM incidents i
WHERE NOT EXISTS (
    SELECT 1
    FROM boundaries b
    WHERE ST_Within(i.location, b.geom)
);

-- Find overlapping boundaries
SELECT 
    b1.name as boundary1,
    b2.name as boundary2
FROM boundaries b1
JOIN boundaries b2 ON b1.id < b2.id
WHERE ST_Overlaps(b1.geom, b2.geom);

-- Validate geometry validity
SELECT 
    id,
    name,
    ST_IsValid(geom) as is_valid,
    ST_IsValidReason(geom) as reason
FROM boundaries
WHERE NOT ST_IsValid(geom);
```

---

## Database Management

### Connection Management

#### Connect to Database

```bash
# Using psql command-line
psql -h localhost -U postgres -d nisircop

# Using Docker
docker exec -it nisircop-db psql -U postgres -d nisircop

# Using environment variables
export PGHOST=localhost
export PGUSER=postgres
export PGDATABASE=nisircop
psql
```

#### Create Database User for Application

```sql
-- Create application user with limited privileges
CREATE USER nisircop_app WITH PASSWORD 'secure_password_here';

-- Grant necessary permissions
GRANT CONNECT ON DATABASE nisircop TO nisircop_app;
GRANT USAGE ON SCHEMA public TO nisircop_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO nisircop_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO nisircop_app;

-- For future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO nisircop_app;
```

### Database Maintenance

#### Analyze Tables (Update Statistics)

```sql
-- Analyze all tables
ANALYZE;

-- Analyze specific table
ANALYZE incidents;

-- Verbose output
ANALYZE VERBOSE incidents;
```

#### Vacuum (Reclaim Storage)

```sql
-- Vacuum all tables
VACUUM;

-- Full vacuum with analyze
VACUUM FULL ANALYZE incidents;

-- Auto-vacuum is enabled by default
```

#### Reindex Spatial Indexes

```sql
-- Reindex spatial indexes for better performance
REINDEX INDEX incidents_location_idx;
REINDEX INDEX boundaries_geom_idx;

-- Reindex all indexes on a table
REINDEX TABLE incidents;
```

### Monitoring Queries

#### Check Database Size

```sql
-- Database size
SELECT 
    pg_size_pretty(pg_database_size('nisircop')) as database_size;

-- Table sizes
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

#### Check Active Connections

```sql
-- Current connections
SELECT 
    pid,
    usename,
    application_name,
    client_addr,
    state,
    query_start,
    state_change
FROM pg_stat_activity
WHERE datname = 'nisircop';

-- Count connections by state
SELECT state, COUNT(*)
FROM pg_stat_activity
WHERE datname = 'nisircop'
GROUP BY state;
```

#### Check Index Usage

```sql
-- Index usage statistics
SELECT 
    schemaname,
    tablename,
    indexname,
    idx_scan as index_scans,
    idx_tup_read as tuples_read,
    idx_tup_fetch as tuples_fetched
FROM pg_stat_user_indexes
WHERE schemaname = 'public'
ORDER BY idx_scan DESC;

-- Find unused indexes
SELECT 
    schemaname,
    tablename,
    indexname
FROM pg_stat_user_indexes
WHERE idx_scan = 0
    AND schemaname = 'public'
    AND indexname NOT LIKE '%_pkey';
```

---

## Backup and Restore

### Backup Database

#### Full Database Backup

```bash
# Backup entire database
pg_dump -h localhost -U postgres -d nisircop -F c -f nisircop_backup.dump

# With compression
pg_dump -h localhost -U postgres -d nisircop -F c -Z 9 -f nisircop_backup.dump

# Plain SQL format
pg_dump -h localhost -U postgres -d nisircop -f nisircop_backup.sql

# Using Docker
docker exec nisircop-db pg_dump -U postgres nisircop > nisircop_backup.sql
```

#### Backup Specific Tables

```bash
# Backup only incidents table
pg_dump -h localhost -U postgres -d nisircop -t incidents -f incidents_backup.sql

# Backup multiple tables
pg_dump -h localhost -U postgres -d nisircop -t users -t incidents -f data_backup.sql
```

#### Automated Daily Backups

```bash
# Create backup script: /usr/local/bin/backup-nisircop.sh
#!/bin/bash
BACKUP_DIR="/var/backups/nisircop"
DATE=$(date +%Y%m%d_%H%M%S)
mkdir -p $BACKUP_DIR

pg_dump -h localhost -U postgres -d nisircop -F c \
    -f $BACKUP_DIR/nisircop_$DATE.dump

# Keep only last 7 days
find $BACKUP_DIR -name "nisircop_*.dump" -mtime +7 -delete
```

```bash
# Add to crontab
crontab -e
# Add line: 0 2 * * * /usr/local/bin/backup-nisircop.sh
```

### Restore Database

#### Restore Full Database

```bash
# Drop and recreate database
psql -h localhost -U postgres -c "DROP DATABASE IF EXISTS nisircop;"
psql -h localhost -U postgres -c "CREATE DATABASE nisircop;"

# Restore from dump
pg_restore -h localhost -U postgres -d nisircop nisircop_backup.dump

# Or from SQL file
psql -h localhost -U postgres -d nisircop -f nisircop_backup.sql
```

#### Restore Specific Tables

```bash
# Restore only users table
pg_restore -h localhost -U postgres -d nisircop -t users nisircop_backup.dump

# Restore with clean (drop existing objects first)
pg_restore -h localhost -U postgres -d nisircop -c -t incidents nisircop_backup.dump
```

### Export/Import Data

#### Export to CSV

```sql
-- Export incidents to CSV
COPY (
    SELECT 
        id,
        title,
        incident_type,
        priority,
        ST_Y(location) as latitude,
        ST_X(location) as longitude,
        occurred_at
    FROM incidents
) TO '/tmp/incidents_export.csv' WITH CSV HEADER;
```

```bash
# Or using psql
psql -h localhost -U postgres -d nisircop -c \
    "COPY incidents TO STDOUT WITH CSV HEADER" > incidents.csv
```

#### Import from CSV

```sql
-- Create temporary table
CREATE TEMP TABLE incidents_import (
    title VARCHAR(255),
    description TEXT,
    incident_type VARCHAR(100),
    priority VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    occurred_at TIMESTAMP
);

-- Import CSV
COPY incidents_import FROM '/tmp/incidents.csv' WITH CSV HEADER;

-- Transform and insert into main table
INSERT INTO incidents (title, description, incident_type, priority, location, occurred_at)
SELECT 
    title,
    description,
    incident_type,
    priority::incident_priority,
    ST_SetSRID(ST_MakePoint(longitude, latitude), 4326),
    occurred_at
FROM incidents_import;
```

---

## Performance Optimization

### Indexing Strategy

```sql
-- Create indexes for common queries
CREATE INDEX idx_incidents_occurred_at ON incidents(occurred_at);
CREATE INDEX idx_incidents_created_at ON incidents(created_at);
CREATE INDEX idx_incidents_type_priority ON incidents(incident_type, priority);

-- Partial indexes for active records
CREATE INDEX idx_active_incidents ON incidents(id) 
WHERE status = 'REPORTED';

-- Expression indexes
CREATE INDEX idx_incidents_date ON incidents(DATE(occurred_at));
```

### Query Optimization

#### Use EXPLAIN ANALYZE

```sql
-- Analyze query performance
EXPLAIN ANALYZE
SELECT i.*
FROM incidents i
JOIN boundaries b ON ST_Within(i.location, b.geom)
WHERE b.name = 'Central Station Zone';

-- Look for:
-- - Sequential Scans (bad for large tables)
-- - Index Scans (good)
-- - Execution time
```

#### Optimize Spatial Queries

```sql
-- Use && operator for bounding box first (faster)
SELECT i.*
FROM incidents i, boundaries b
WHERE b.id = 1
    AND i.location && b.geom  -- Bounding box check (fast)
    AND ST_Within(i.location, b.geom);  -- Exact check (slower)
```

### Connection Pooling

Configure in `application.properties`:

```properties
# HikariCP settings
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### PostgreSQL Configuration

Edit `postgresql.conf`:

```conf
# Memory settings
shared_buffers = 256MB
effective_cache_size = 1GB
work_mem = 16MB
maintenance_work_mem = 128MB

# Connections
max_connections = 100

# Autovacuum
autovacuum = on
autovacuum_max_workers = 3
```

---

## Troubleshooting

### Common Issues

#### 1. Cannot Connect to Database

```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql

# Check if port is open
netstat -an | grep 5432

# Test connection
psql -h localhost -U postgres -d nisircop

# Check pg_hba.conf for authentication rules
sudo nano /etc/postgresql/15/main/pg_hba.conf
```

#### 2. PostGIS Functions Not Working

```sql
-- Check if PostGIS is installed
SELECT postgis_version();

-- If not, install it
CREATE EXTENSION IF NOT EXISTS postgis;

-- Check available extensions
SELECT * FROM pg_available_extensions WHERE name LIKE 'postgis%';
```

#### 3. Slow Spatial Queries

```sql
-- Check if spatial indexes exist
SELECT 
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE indexdef LIKE '%GIST%';

-- Recreate spatial indexes if missing
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);
CREATE INDEX boundaries_geom_idx ON boundaries USING GIST (geom);

-- Analyze tables
ANALYZE incidents;
ANALYZE boundaries;
```

#### 4. Invalid Geometry Errors

```sql
-- Find invalid geometries
SELECT id, name, ST_IsValidReason(geom)
FROM boundaries
WHERE NOT ST_IsValid(geom);

-- Fix invalid geometries
UPDATE boundaries
SET geom = ST_MakeValid(geom)
WHERE NOT ST_IsValid(geom);
```

#### 5. Out of Memory Errors

```sql
-- Check current memory usage
SELECT 
    pg_size_pretty(pg_total_relation_size('incidents')) as incidents_size,
    pg_size_pretty(pg_total_relation_size('boundaries')) as boundaries_size;

-- Vacuum to reclaim space
VACUUM FULL ANALYZE;
```

### Debug Queries

```sql
-- Show current queries
SELECT 
    pid,
    now() - query_start as duration,
    query
FROM pg_stat_activity
WHERE state = 'active'
ORDER BY duration DESC;

-- Kill long-running query
SELECT pg_cancel_backend(pid);  -- Graceful
SELECT pg_terminate_backend(pid);  -- Forceful

-- Show table bloat
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as total_size,
    n_live_tup as live_rows,
    n_dead_tup as dead_rows
FROM pg_stat_user_tables
ORDER BY n_dead_tup DESC;
```

---

## Sample Data and Testing

### Generate Test Data

```sql
-- Insert 100 random incidents
INSERT INTO incidents (title, incident_type, priority, location, reported_by, occurred_at)
SELECT 
    'Test Incident ' || generate_series,
    (ARRAY['THEFT', 'ROBBERY', 'VANDALISM', 'ASSAULT'])[floor(random() * 4 + 1)],
    (ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'])[floor(random() * 4 + 1)]::incident_priority,
    ST_SetSRID(ST_MakePoint(
        38.7 + random() * 0.1,  -- Random longitude around Addis Ababa
        9.0 + random() * 0.1    -- Random latitude
    ), 4326),
    3,  -- Reported by officer ID 3
    CURRENT_TIMESTAMP - (random() * INTERVAL '30 days')
FROM generate_series(1, 100);
```

### Test Spatial Functions

```sql
-- Test point-in-polygon
WITH test_point AS (
    SELECT ST_SetSRID(ST_MakePoint(38.75, 9.05), 4326) as geom
)
SELECT 
    b.name,
    ST_Within(tp.geom, b.geom) as contains_point
FROM test_point tp
CROSS JOIN boundaries b;

-- Test distance calculations
SELECT 
    i1.title as incident1,
    i2.title as incident2,
    ST_Distance(i1.location::geography, i2.location::geography) as distance_meters
FROM incidents i1
CROSS JOIN incidents i2
WHERE i1.id < i2.id
ORDER BY distance_meters
LIMIT 10;
```

---

## Additional Resources

### PostGIS Documentation
- [PostGIS Reference](https://postgis.net/docs/reference.html)
- [PostGIS Spatial Relationships](https://postgis.net/docs/reference.html#Spatial_Relationships)
- [PostGIS Functions](https://postgis.net/docs/reference.html#Spatial_Functions)

### PostgreSQL Documentation
- [PostgreSQL Manual](https://www.postgresql.org/docs/15/index.html)
- [Performance Tips](https://wiki.postgresql.org/wiki/Performance_Optimization)
- [Backup and Restore](https://www.postgresql.org/docs/15/backup.html)

### Tools
- **pgAdmin 4**: GUI for PostgreSQL management
- **DBeaver**: Universal database tool
- **QGIS**: Desktop GIS for visualizing spatial data
- **PostGIS Shapefile Loader**: Import shapefiles

---

## Quick Reference Card

```sql
-- Common Commands
\l                  -- List databases
\c nisircop        -- Connect to database
\dt                 -- List tables
\d incidents        -- Describe table
\di                 -- List indexes
\q                  -- Quit

-- Spatial Operations
ST_MakePoint(lng, lat)              -- Create point
ST_GeomFromText('POLYGON(...)')     -- Create polygon from WKT
ST_Within(point, polygon)           -- Point in polygon check
ST_Distance(geom1, geom2)           -- Distance between geometries
ST_AsGeoJSON(geom)                  -- Export as GeoJSON
ST_X(point), ST_Y(point)           -- Extract coordinates

-- Common Queries
-- Total incidents: SELECT COUNT(*) FROM incidents;
-- Incidents today: SELECT * FROM incidents WHERE created_at::date = CURRENT_DATE;
-- All users: SELECT username, role, full_name FROM users;
-- Boundaries: SELECT name, ST_AsText(geom) FROM boundaries;
```

---

**Need help?** Check the [README.md](README.md) for application setup or [LEARN.md](LEARN.md) for comprehensive tutorials.
