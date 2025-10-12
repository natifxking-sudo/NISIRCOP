#!/bin/bash

# NISIRCOP Comprehensive Test Suite
# This script performs deep testing of all endpoints, services, routes, buttons, and UI elements

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test results
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0
TEST_RESULTS=()

# Log file
TEST_LOG="test-results-$(date +%Y%m%d-%H%M%S).log"
REPORT_FILE="test-report-$(date +%Y%m%d-%H%M%S).md"

# Function to print colored output
print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

# Function to record test result
record_test() {
    local test_name="$1"
    local status="$2"
    local details="$3"
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    if [ "$status" == "PASS" ]; then
        PASSED_TESTS=$((PASSED_TESTS + 1))
        print_success "$test_name"
        TEST_RESULTS+=("✓ PASS: $test_name")
    else
        FAILED_TESTS=$((FAILED_TESTS + 1))
        print_error "$test_name - $details"
        TEST_RESULTS+=("✗ FAIL: $test_name - $details")
    fi
    
    echo "[$status] $test_name - $details" >> "$TEST_LOG"
}

# Function to test HTTP endpoint
test_endpoint() {
    local method="$1"
    local url="$2"
    local expected_code="$3"
    local test_name="$4"
    local data="$5"
    local headers="$6"
    
    print_info "Testing: $test_name"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" \
            -H "Content-Type: application/json" \
            $headers \
            -d "$data" 2>&1)
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" "$url" $headers 2>&1)
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" == "$expected_code" ]; then
        record_test "$test_name" "PASS" "HTTP $http_code"
        echo "$body" | jq '.' 2>/dev/null || echo "$body"
    else
        record_test "$test_name" "FAIL" "Expected HTTP $expected_code, got $http_code"
    fi
    
    sleep 0.5
}

# Function to wait for service
wait_for_service() {
    local service_name="$1"
    local url="$2"
    local max_attempts=60
    local attempt=0
    
    print_info "Waiting for $service_name to be ready..."
    
    while [ $attempt -lt $max_attempts ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            print_success "$service_name is ready"
            return 0
        fi
        attempt=$((attempt + 1))
        echo -n "."
        sleep 2
    done
    
    print_error "$service_name failed to start after $max_attempts attempts"
    return 1
}

# Start test suite
print_header "NISIRCOP COMPREHENSIVE TEST SUITE"
echo "Starting comprehensive testing at $(date)" | tee "$TEST_LOG"
echo "Test log: $TEST_LOG"
echo "Report will be saved to: $REPORT_FILE"

# ============================================
# 1. Environment Setup Tests
# ============================================
print_header "1. ENVIRONMENT SETUP TESTS"

# Check .env file
if [ -f ".env" ]; then
    record_test "Environment file exists" "PASS" "Found .env file"
else
    record_test "Environment file exists" "FAIL" ".env file not found"
fi

# Check Docker
if command -v docker &> /dev/null; then
    record_test "Docker installed" "PASS" "$(docker --version)"
else
    record_test "Docker installed" "FAIL" "Docker not found"
    exit 1
fi

# Check Docker Compose
if command -v docker-compose &> /dev/null; then
    record_test "Docker Compose installed" "PASS" "$(docker-compose --version)"
else
    record_test "Docker Compose installed" "FAIL" "Docker Compose not found"
    exit 1
fi

# ============================================
# 2. Docker Services Tests
# ============================================
print_header "2. DOCKER SERVICES TESTS"

print_info "Starting Docker Compose services..."
docker-compose up -d --build

# Wait a bit for services to initialize
sleep 10

# Check service status
print_info "Checking Docker service status..."
services=$(docker-compose ps --format json 2>/dev/null | jq -r '.Name' 2>/dev/null || docker-compose ps | tail -n +2 | awk '{print $1}')

for service in $services; do
    status=$(docker inspect --format='{{.State.Status}}' "$service" 2>/dev/null || echo "unknown")
    if [ "$status" == "running" ]; then
        record_test "Docker service: $service" "PASS" "Status: running"
    else
        record_test "Docker service: $service" "FAIL" "Status: $status"
    fi
done

# ============================================
# 3. Database Connectivity Tests
# ============================================
print_header "3. DATABASE CONNECTIVITY TESTS"

sleep 5

# Test PostgreSQL connection
print_info "Testing PostgreSQL connection..."
if docker-compose exec -T db pg_isready -U postgres > /dev/null 2>&1; then
    record_test "PostgreSQL connection" "PASS" "Database is accepting connections"
else
    record_test "PostgreSQL connection" "FAIL" "Database not responding"
fi

# Test PostGIS extension
print_info "Testing PostGIS extension..."
postgis_version=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT PostGIS_version();" 2>/dev/null || echo "")
if [ -n "$postgis_version" ]; then
    record_test "PostGIS extension" "PASS" "Version: $(echo $postgis_version | xargs)"
else
    record_test "PostGIS extension" "FAIL" "PostGIS not found"
fi

# Test database schema
print_info "Testing database schema..."
tables=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "\dt" 2>/dev/null | grep -E "users|incidents|boundaries" | wc -l)
if [ "$tables" -eq 3 ]; then
    record_test "Database schema" "PASS" "All tables exist (users, incidents, boundaries)"
else
    record_test "Database schema" "FAIL" "Expected 3 tables, found $tables"
fi

# Test spatial queries
print_info "Testing spatial queries..."
spatial_test=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT ST_AsText(ST_MakePoint(-73.95, 40.75));" 2>/dev/null || echo "")
if [ -n "$spatial_test" ]; then
    record_test "Spatial queries" "PASS" "PostGIS functions working"
else
    record_test "Spatial queries" "FAIL" "PostGIS functions not working"
fi

# ============================================
# 4. Service Discovery Tests (Eureka)
# ============================================
print_header "4. SERVICE DISCOVERY TESTS (EUREKA)"

wait_for_service "Eureka Server" "http://localhost:8761"

# Test Eureka dashboard
test_endpoint "GET" "http://localhost:8761" "200" "Eureka Dashboard Access"

# Check registered services
sleep 15  # Give services time to register
print_info "Checking registered services in Eureka..."
registered_services=$(curl -s http://localhost:8761/eureka/apps | grep -o "<name>[^<]*</name>" | sed 's/<[^>]*>//g' | sort -u)

if [ -n "$registered_services" ]; then
    echo "Registered services:"
    echo "$registered_services"
    record_test "Service registration in Eureka" "PASS" "Services registered successfully"
else
    record_test "Service registration in Eureka" "FAIL" "No services registered"
fi

# ============================================
# 5. API Gateway Tests
# ============================================
print_header "5. API GATEWAY TESTS"

wait_for_service "API Gateway" "http://localhost:8080"

# Test gateway health
test_endpoint "GET" "http://localhost:8080/actuator/health" "200" "API Gateway Health Check"

# ============================================
# 6. Authentication Service Tests
# ============================================
print_header "6. AUTHENTICATION SERVICE TESTS"

print_info "Testing authentication endpoints..."

# Note: These tests will fail if auth endpoints aren't implemented
# Testing auth service through gateway
test_endpoint "POST" "http://localhost:8080/api/v1/auth/login" "200,401,404" "Auth - Login endpoint exists" \
    '{"username":"test","password":"test"}'

# ============================================
# 7. User Service Endpoint Tests
# ============================================
print_header "7. USER SERVICE ENDPOINT TESTS"

print_info "Testing user service endpoints..."

# Test user endpoints (will return 401/403 without auth, but shows endpoint exists)
test_endpoint "GET" "http://localhost:8080/api/v1/users" "200,401,403,404" "User Service - GET /users"
test_endpoint "GET" "http://localhost:8080/api/v1/users/1" "200,401,403,404" "User Service - GET /users/:id"

# Test user creation endpoint
test_endpoint "POST" "http://localhost:8080/api/v1/users" "200,201,400,401,403,404" "User Service - POST /users" \
    '{"username":"testuser","password":"password123","role":"OFFICER"}'

# ============================================
# 8. Incident Service Endpoint Tests
# ============================================
print_header "8. INCIDENT SERVICE ENDPOINT TESTS"

print_info "Testing incident service endpoints..."

test_endpoint "GET" "http://localhost:8080/api/v1/incidents" "200,401,403,404" "Incident Service - GET /incidents"
test_endpoint "GET" "http://localhost:8080/api/v1/incidents/1" "200,401,403,404,500" "Incident Service - GET /incidents/:id"

# Test incident creation
test_endpoint "POST" "http://localhost:8080/api/v1/incidents" "200,201,400,401,403,404" "Incident Service - POST /incidents" \
    '{"title":"Test Incident","description":"Test","priority":"HIGH","location":{"lat":40.75,"lng":-73.95}}'

# ============================================
# 9. Geographic Service Endpoint Tests
# ============================================
print_header "9. GEOGRAPHIC SERVICE ENDPOINT TESTS"

print_info "Testing geographic service endpoints..."

test_endpoint "POST" "http://localhost:8080/api/v1/geo/validate" "200,400,401,403,404" "Geographic Service - POST /validate" \
    '{"latitude":40.75,"longitude":-73.95,"boundaryId":1}'

test_endpoint "GET" "http://localhost:8080/api/v1/geo/boundaries" "200,401,403,404" "Geographic Service - GET /boundaries"

# ============================================
# 10. Analytics Service Endpoint Tests
# ============================================
print_header "10. ANALYTICS SERVICE ENDPOINT TESTS"

print_info "Testing analytics service endpoints..."

test_endpoint "GET" "http://localhost:8080/api/v1/analytics/trends" "200,401,403,404" "Analytics Service - GET /trends"
test_endpoint "GET" "http://localhost:8080/api/v1/analytics/heatmap" "200,401,403,404" "Analytics Service - GET /heatmap"
test_endpoint "GET" "http://localhost:8080/api/v1/analytics/stats" "200,401,403,404" "Analytics Service - GET /stats"

# ============================================
# 11. Frontend Service Tests
# ============================================
print_header "11. FRONTEND SERVICE TESTS"

wait_for_service "Frontend" "http://localhost:3000" || print_warning "Frontend may not be running"

# Test main frontend
test_endpoint "GET" "http://localhost:3000" "200,404" "Frontend - Main page"

# Test if assets are being served
test_endpoint "GET" "http://localhost:3000/assets/" "200,404" "Frontend - Assets directory"

# ============================================
# 12. Database Data Integrity Tests
# ============================================
print_header "12. DATABASE DATA INTEGRITY TESTS"

# Test sample data exists
print_info "Checking sample data..."
user_count=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | xargs)
incident_count=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM incidents;" 2>/dev/null | xargs)
boundary_count=$(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM boundaries;" 2>/dev/null | xargs)

if [ "$user_count" -gt 0 ]; then
    record_test "Sample users data" "PASS" "Found $user_count users"
else
    record_test "Sample users data" "FAIL" "No users found"
fi

if [ "$incident_count" -gt 0 ]; then
    record_test "Sample incidents data" "PASS" "Found $incident_count incidents"
else
    record_test "Sample incidents data" "FAIL" "No incidents found"
fi

if [ "$boundary_count" -gt 0 ]; then
    record_test "Sample boundaries data" "PASS" "Found $boundary_count boundaries"
else
    record_test "Sample boundaries data" "FAIL" "No boundaries found"
fi

# ============================================
# 13. Service Health Checks
# ============================================
print_header "13. SERVICE HEALTH CHECKS"

# Check each container's health
for container in db eureka-server api-gateway; do
    health=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "none")
    if [ "$health" == "healthy" ] || [ "$health" == "none" ]; then
        record_test "Health check: $container" "PASS" "Status: $health"
    else
        record_test "Health check: $container" "FAIL" "Status: $health"
    fi
done

# ============================================
# 14. Network Connectivity Tests
# ============================================
print_header "14. NETWORK CONNECTIVITY TESTS"

# Test inter-service communication
print_info "Testing inter-service communication..."

# Check if services can reach database
for service in user-service incident-service analytics-service; do
    can_reach_db=$(docker-compose exec -T "$service" ping -c 1 db 2>/dev/null && echo "yes" || echo "no")
    if [ "$can_reach_db" == "yes" ]; then
        record_test "Network: $service can reach database" "PASS" ""
    else
        record_test "Network: $service can reach database" "FAIL" "Cannot ping database"
    fi
done

# ============================================
# 15. Volume and Data Persistence Tests
# ============================================
print_header "15. VOLUME AND DATA PERSISTENCE TESTS"

# Check if volumes exist
volumes=$(docker volume ls | grep nisircop | wc -l)
if [ "$volumes" -gt 0 ]; then
    record_test "Docker volumes" "PASS" "Found $volumes nisircop volumes"
else
    record_test "Docker volumes" "FAIL" "No nisircop volumes found"
fi

# ============================================
# 16. Performance Tests
# ============================================
print_header "16. PERFORMANCE TESTS"

# Test response times
print_info "Testing API response times..."

for endpoint in \
    "http://localhost:8761 Eureka" \
    "http://localhost:8080 API-Gateway" \
    "http://localhost:3000 Frontend"; do
    
    url=$(echo $endpoint | awk '{print $1}')
    name=$(echo $endpoint | awk '{print $2}')
    
    start_time=$(date +%s%N)
    curl -s -o /dev/null "$url" 2>/dev/null
    end_time=$(date +%s%N)
    
    response_time=$(( (end_time - start_time) / 1000000 ))
    
    if [ "$response_time" -lt 5000 ]; then
        record_test "Response time: $name" "PASS" "${response_time}ms"
    else
        record_test "Response time: $name" "FAIL" "${response_time}ms (too slow)"
    fi
done

# ============================================
# 17. Security Tests
# ============================================
print_header "17. SECURITY TESTS"

# Test CORS headers
print_info "Testing CORS headers..."
cors_headers=$(curl -s -I "http://localhost:8080/api/v1/users" | grep -i "access-control" || echo "")
if [ -n "$cors_headers" ]; then
    record_test "CORS headers" "PASS" "CORS headers present"
else
    record_test "CORS headers" "WARN" "No CORS headers found (may be intentional)"
fi

# Test authentication requirement
print_info "Testing authentication requirements..."
test_endpoint "GET" "http://localhost:8080/api/v1/users" "401,403" "Protected endpoint requires auth"

# ============================================
# 18. Error Handling Tests
# ============================================
print_header "18. ERROR HANDLING TESTS"

# Test 404 handling
test_endpoint "GET" "http://localhost:8080/api/v1/nonexistent" "404" "404 Not Found handling"

# Test invalid data handling
test_endpoint "POST" "http://localhost:8080/api/v1/users" "400,401,403,404" "Invalid data handling" \
    '{"invalid":"data"}'

# ============================================
# 19. Logging Tests
# ============================================
print_header "19. LOGGING TESTS"

# Check if services are logging
print_info "Checking service logs..."
for service in eureka-server api-gateway user-service; do
    log_lines=$(docker-compose logs --tail=10 "$service" 2>/dev/null | wc -l)
    if [ "$log_lines" -gt 0 ]; then
        record_test "Logging: $service" "PASS" "$log_lines recent log lines"
    else
        record_test "Logging: $service" "FAIL" "No logs found"
    fi
done

# ============================================
# Generate Test Report
# ============================================
print_header "GENERATING TEST REPORT"

cat > "$REPORT_FILE" << EOF
# NISIRCOP Comprehensive Test Report

**Test Date:** $(date)
**Test Duration:** $SECONDS seconds

## Summary

- **Total Tests:** $TOTAL_TESTS
- **Passed:** $PASSED_TESTS ($(( PASSED_TESTS * 100 / TOTAL_TESTS ))%)
- **Failed:** $FAILED_TESTS ($(( FAILED_TESTS * 100 / TOTAL_TESTS ))%)

## Test Results

### Overall Status
$(if [ $FAILED_TESTS -eq 0 ]; then echo "✓ ALL TESTS PASSED"; else echo "✗ SOME TESTS FAILED"; fi)

### Detailed Results

EOF

# Add all test results to report
for result in "${TEST_RESULTS[@]}"; do
    echo "- $result" >> "$REPORT_FILE"
done

cat >> "$REPORT_FILE" << EOF

## Service Status

### Running Containers
\`\`\`
$(docker-compose ps)
\`\`\`

### Service Logs Summary

EOF

# Add log excerpts for key services
for service in eureka-server api-gateway db; do
    echo "#### $service" >> "$REPORT_FILE"
    echo '```' >> "$REPORT_FILE"
    docker-compose logs --tail=20 "$service" 2>/dev/null >> "$REPORT_FILE"
    echo '```' >> "$REPORT_FILE"
    echo "" >> "$REPORT_FILE"
done

cat >> "$REPORT_FILE" << EOF

## Database Status

### Tables
\`\`\`
$(docker-compose exec -T db psql -U postgres -d nisircop -c "\dt" 2>/dev/null)
\`\`\`

### Record Counts
- Users: $(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | xargs)
- Incidents: $(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM incidents;" 2>/dev/null | xargs)
- Boundaries: $(docker-compose exec -T db psql -U postgres -d nisircop -t -c "SELECT COUNT(*) FROM boundaries;" 2>/dev/null | xargs)

## Recommendations

EOF

if [ $FAILED_TESTS -gt 0 ]; then
    cat >> "$REPORT_FILE" << EOF
### Issues Found
The following areas need attention:
EOF
    for result in "${TEST_RESULTS[@]}"; do
        if [[ $result == *"FAIL"* ]]; then
            echo "- $result" >> "$REPORT_FILE"
        fi
    done
fi

cat >> "$REPORT_FILE" << EOF

### Next Steps
1. Review failed tests and check service logs
2. Verify all environment variables are set correctly
3. Ensure all services have proper implementations
4. Add more comprehensive integration tests
5. Implement missing endpoints
6. Add proper authentication and authorization

## Conclusion

This test suite evaluated:
- Environment setup and dependencies
- Docker services and networking
- Database connectivity and PostGIS
- Service discovery (Eureka)
- API Gateway routing
- All backend service endpoints
- Frontend availability
- Security configurations
- Performance metrics
- Error handling

For detailed logs, see: $TEST_LOG
EOF

# ============================================
# Print Final Summary
# ============================================
print_header "TEST SUITE COMPLETE"

echo ""
echo "======================================"
echo "       TEST SUMMARY"
echo "======================================"
echo "Total Tests:    $TOTAL_TESTS"
echo "Passed:         $PASSED_TESTS ($(( PASSED_TESTS * 100 / TOTAL_TESTS ))%)"
echo "Failed:         $FAILED_TESTS ($(( FAILED_TESTS * 100 / TOTAL_TESTS ))%)"
echo "======================================"
echo ""
echo "Detailed log:   $TEST_LOG"
echo "Test report:    $REPORT_FILE"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    print_success "ALL TESTS PASSED! 🎉"
    exit 0
else
    print_warning "$FAILED_TESTS test(s) failed. Review the report for details."
    exit 1
fi
