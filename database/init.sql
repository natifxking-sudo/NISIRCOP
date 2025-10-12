-- Create the database (if it doesn't exist, though docker-compose handles this)
-- CREATE DATABASE nisircop;

-- Connect to the database
-- \c nisircop;

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

-- Define ENUM types for roles and incident properties
CREATE TYPE user_role AS ENUM ('OFFICER', 'POLICE_STATION', 'SUPER_USER');
CREATE TYPE incident_priority AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL');

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    full_name VARCHAR(100),
    station_id INT, -- Can be linked to a boundaries/stations table later
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create boundaries table for police station jurisdictions
CREATE TABLE boundaries (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    geom GEOMETRY(Polygon, 4326) NOT NULL -- SRID 4326 for WGS 84
);

-- Create a spatial index on the geometry column
CREATE INDEX boundaries_geom_idx ON boundaries USING GIST (geom);

-- Create incidents table
CREATE TABLE incidents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    incident_type VARCHAR(100),
    priority incident_priority NOT NULL,
    location GEOMETRY(Point, 4326) NOT NULL, -- SRID 4326 for WGS 84
    reported_by INT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create a spatial index on the location column
CREATE INDEX incidents_location_idx ON incidents USING GIST (location);

-- Insert Sample Data

-- Users (passwords are placeholders and should be hashed in a real app)
-- Password for all is 'password' (to be hashed by the application)
INSERT INTO users (username, password, role, full_name, station_id) VALUES
('super_user', '$2a$10$e.ExV8sY.s/5/DaJ4WYRz.oO/vBE3g0fAC5.WfXQ.Lz/jd.3r/J7a', 'SUPER_USER', 'Admin User', NULL),
('station_one', '$2a$10$e.ExV8sY.s/5/DaJ4WYRz.oO/vBE3g0fAC5.WfXQ.Lz/jd.3r/J7a', 'POLICE_STATION', 'Central Station Admin', 1),
('officer_jane', '$2a$10$e.ExV8sY.s/5/DaJ4WYRz.oO/vBE3g0fAC5.WfXQ.Lz/jd.3r/J7a', 'OFFICER', 'Jane Doe', 1),
('officer_john', '$2a$10$e.ExV8sY.s/5/DaJ4WYRz.oO/vBE3g0fAC5.WfXQ.Lz/jd.3r/J7a', 'OFFICER', 'John Smith', 1);

-- Boundaries (Sample polygon for a fictional city area)
-- Coordinates are (longitude latitude)
INSERT INTO boundaries (id, name, geom) VALUES
(1, 'Central Station Zone', ST_GeomFromText('POLYGON((-74.0 40.7, -74.0 40.8, -73.9 40.8, -73.9 40.7, -74.0 40.7))', 4326));

-- Incidents (Sample incidents within the Central Station Zone)
INSERT INTO incidents (title, description, incident_type, priority, location, reported_by) VALUES
('Robbery at Main St', 'Armed robbery reported at the corner of Main St and 1st Ave.', 'Robbery', 'CRITICAL', ST_SetSRID(ST_MakePoint(-73.95, 40.75), 4326), 3),
('Vandalism at City Park', 'Graffiti reported on the park statues.', 'Vandalism', 'LOW', ST_SetSRID(ST_MakePoint(-73.98, 40.76), 4326), 4),
('Suspicious Package', 'A suspicious package was found near the subway entrance.', 'Public Hazard', 'HIGH', ST_SetSRID(ST_MakePoint(-73.92, 40.78), 4326), 3);
