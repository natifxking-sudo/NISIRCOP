# NISIRCOP User Manual

Welcome to the **NISIRCOP-LE-ANALYTICS** platform - a modern crime analytics and incident management system for law enforcement agencies.

## Table of Contents
1. [Getting Started](#1-getting-started)
2. [For Officers](#2-for-officers)
3. [For Police Station Administrators](#3-for-police_station-users)
4. [For System Administrators](#4-for-super_users)
5. [Troubleshooting](#5-troubleshooting)

## 1. Getting Started

### 1.1 Accessing the System

**URL**: Navigate to `http://localhost:3000` (or your organization's deployed URL)

### 1.2 Logging In

1. Enter your **username** and **password** on the login page
2. Click **Login**
3. Upon successful authentication, you'll be redirected to your role-specific dashboard

**Demo Credentials** (for testing):
- **Super User**: Username: `super_user`, Password: `password`
- **Station Admin**: Username: `station_one`, Password: `password`
- **Officer**: Username: `officer_jane`, Password: `password`

⚠️ **Security Note**: Change default passwords after first login in production environments!

### 1.3 System Overview

NISIRCOP provides:
- ✅ **Real-time incident reporting** with geolocation
- ✅ **Role-based access control** (Officer, Station Admin, Super User)
- ✅ **Interactive maps** with crime incident visualization
- ✅ **Analytics dashboard** with trends and statistics
- ✅ **User management** for station administrators
- ✅ **Geographic boundary management** for jurisdictions

## 2. For OFFICERS

Officers are front-line users who report and manage incidents in the field.

### 2.1 Reporting an Incident

1. **Navigate** to the "Incidents" module from the main menu
2. Click **"Report New Incident"** button
3. **Fill in the incident form**:
   - **Title**: Brief description (e.g., "Robbery at Main St")
   - **Incident Type**: Select from dropdown (Robbery, Vandalism, etc.)
   - **Priority**: Choose severity (LOW, MEDIUM, HIGH, CRITICAL)
   - **Location**: Click on the map or enter coordinates
   - **Description**: Detailed incident information
4. Click **"Submit"** to create the incident record

**Tips**:
- Use accurate location data - this is crucial for analytics
- Select appropriate priority levels
- Include detailed descriptions for better follow-up

### 2.2 Viewing Incidents

**Your Incidents**:
- Dashboard shows all incidents you've reported
- Filter by date, priority, or status
- Click on any incident to view full details

**Search & Filter**:
- Use the search bar to find specific incidents
- Filter by:
  - Date range
  - Priority level
  - Incident type
  - Status (if implemented)

### 2.3 Incident Map View

- View all incidents on an interactive map
- Color-coded markers indicate priority levels:
  - 🔴 Red: CRITICAL
  - 🟠 Orange: HIGH
  - 🟡 Yellow: MEDIUM
  - 🟢 Green: LOW
- Click markers to see incident details

## 3. For POLICE_STATION Users

Station administrators manage officers and monitor jurisdictional activity.

### 3.1 Managing Officers

**Creating Officer Accounts**:
1. Navigate to **"Users"** module
2. Click **"Create New User"**
3. Fill in officer details:
   - Username (must be unique)
   - Full name
   - Initial password
   - Role: Select "OFFICER"
   - Station assignment
4. Click **"Create"**
5. Provide credentials to the new officer securely

**Managing Existing Officers**:
- View list of all officers in your station
- Update officer information
- Deactivate accounts when needed (if feature is implemented)

**Best Practices**:
- Use standardized username formats (e.g., officer_firstname)
- Require officers to change passwords on first login
- Regularly review officer access

### 3.2 Monitoring Station Activity

**Incident Dashboard**:
- View all incidents within your jurisdiction
- Real-time updates as incidents are reported
- Filter and sort by various criteria

**Geographic View**:
- See incidents plotted on your station's boundary map
- Identify crime hotspots
- Assess resource allocation needs

**Analytics Access**:
- View trends specific to your station
- Compare current period vs. historical data
- Export reports for meetings and planning

### 3.3 Jurisdiction Management

- View your station's geographic boundaries
- Understand coverage area
- Coordinate with neighboring stations

## 4. For SUPER_USERs

Super users have full system access and manage organization-wide settings.

### 4.1 System-Wide Administration

**Complete Access**:
- ✅ View and manage ALL users across all stations
- ✅ Create POLICE_STATION administrator accounts
- ✅ Access all incidents system-wide
- ✅ Configure geographic boundaries
- ✅ View comprehensive analytics

**User Management**:
1. Navigate to **"Users"** module
2. View users from all stations
3. Create new users with any role:
   - **OFFICER**: Basic incident reporting
   - **POLICE_STATION**: Station administration
   - **SUPER_USER**: System administration
4. Manage or deactivate any user account

**Station Setup**:
- Create new police station accounts
- Assign boundaries/jurisdictions
- Configure station-specific settings

### 4.2 System-Wide Analytics

**Comprehensive Dashboard**:
- View crime statistics across ALL jurisdictions
- Compare performance between stations
- Identify citywide/regionwide trends

**Strategic Insights**:
- **Temporal Analysis**: Crime trends over time
- **Geographic Analysis**: Hotspot identification across boundaries
- **Type Analysis**: Most common incident types
- **Priority Distribution**: Resource allocation insights

**Reporting**:
- Generate reports for:
  - Executive briefings
  - Budget planning
  - Resource allocation
  - Policy decisions

### 4.3 System Configuration

**Database Management**:
- Monitor system health
- View database statistics
- Ensure data integrity

**Service Monitoring**:
- Check microservice health at `http://localhost:8761` (Eureka Dashboard)
- View all registered services
- Monitor service status

### 4.4 Security Management

**Best Practices**:
- Regularly audit user accounts
- Review access logs
- Update security configurations
- Manage JWT token expiration settings
- Monitor for suspicious activity

## 5. Troubleshooting

### 5.1 Login Issues

**Problem**: Cannot log in
**Solutions**:
- Verify username and password (case-sensitive)
- Check with your administrator if account is active
- Clear browser cache and cookies
- Try a different browser

### 5.2 Map Not Loading

**Problem**: Geographic map not displaying
**Solutions**:
- Check internet connection (maps require external resources)
- Refresh the page
- Disable browser extensions that might block content
- Try a different browser

### 5.3 Incident Not Appearing

**Problem**: Submitted incident doesn't show up
**Solutions**:
- Wait a few seconds and refresh the page
- Check that you're viewing the correct filters (date range, status)
- Verify the incident was submitted successfully (check for confirmation message)
- Contact your administrator if issue persists

### 5.4 Permission Denied

**Problem**: "Access Denied" or "Insufficient Permissions" error
**Solutions**:
- You may be trying to access a feature not available for your role
- Officers cannot access user management
- Check with your administrator about your account permissions

### 5.5 Technical Support

For technical issues:
1. **Document the issue**:
   - What you were trying to do
   - Error messages received
   - Time when issue occurred
2. **Contact your system administrator**
3. **Provide relevant details**:
   - Your username (never your password!)
   - Browser and version
   - Screenshots if possible

## 6. Tips for Effective Use

### 6.1 For All Users

- ✅ **Log out** when finished, especially on shared computers
- ✅ **Use strong passwords** and change them regularly
- ✅ **Report issues** promptly to administrators
- ✅ **Keep browser updated** for best performance
- ✅ **Be accurate** with data entry - quality matters!

### 6.2 For Officers

- 📍 Always include accurate **location data**
- 📝 Write **detailed descriptions** - they help investigations
- ⚡ Set appropriate **priority levels** for efficient response
- 📅 Report incidents **promptly** for accurate time tracking

### 6.3 For Administrators

- 👥 **Audit user accounts** regularly
- 📊 Review **analytics weekly** to identify trends
- 🎓 **Train officers** on proper data entry
- 🔐 Enforce **password policies**
- 📋 Generate **regular reports** for stakeholders

## 7. System Architecture (For Reference)

NISIRCOP uses a modern **microservices architecture**:

- **Frontend** (Port 3000): User interface you interact with
- **API Gateway** (Port 8080): Routes your requests
- **Backend Services**: Handle specific functions:
  - Auth Service: Login and security
  - User Service: User management
  - Incident Service: Incident data
  - Geographic Service: Map and boundaries
  - Analytics Service: Statistics and trends
- **Database**: PostgreSQL with PostGIS for spatial data

All services are **automatically load-balanced** and **highly available**.

## 8. Security & Privacy

### 8.1 Data Protection

- All passwords are **encrypted** using BCrypt
- Communications use **secure protocols**
- **JWT tokens** expire after 24 hours for security
- Database has **backup and recovery** procedures

### 8.2 Access Control

- **Role-based permissions** ensure data privacy
- Officers see only relevant data
- Station admins see only their jurisdiction
- Super users have full access for oversight

### 8.3 Compliance

- System designed with **data protection** in mind
- **Audit trails** for accountability
- **Access logs** for security monitoring

---

**For additional support or questions**, contact your system administrator or refer to the technical documentation in the `/documentation` folder.
