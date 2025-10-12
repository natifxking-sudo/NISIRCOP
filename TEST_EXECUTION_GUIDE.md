# Test Execution Guide for NISIRCOP-LE-ANALYTICS

## Overview
This guide explains how to run the comprehensive test suites created for the NISIRCOP project.

## Available Test Suites

### 1. Comprehensive Test Suite (Requires Docker)
**File:** `comprehensive-test-suite.sh`
**Purpose:** Full integration testing with running Docker containers

```bash
# Run the comprehensive test suite
bash comprehensive-test-suite.sh
```

**This test suite includes:**
- Environment setup validation
- Docker service health checks
- Database connectivity testing
- PostGIS spatial query testing
- Service discovery (Eureka) testing
- API Gateway testing
- All backend service endpoint testing
- Frontend accessibility testing
- Network connectivity testing
- Performance testing
- Security testing

**Prerequisites:**
- Docker and Docker Compose installed
- .env file configured
- Sufficient system resources (8GB+ RAM recommended)

**Execution Time:** ~5-10 minutes

---

### 2. Static Analysis Test Suite (No Docker Required)
**File:** `static-analysis-test.sh`
**Purpose:** Code structure and configuration validation

```bash
# Run the static analysis test suite
bash static-analysis-test.sh
```

**This test suite includes:**
- Project structure verification
- Configuration file validation
- Backend service structure checks
- Frontend application structure checks
- Documentation verification
- Code quality metrics
- Security configuration checks
- API endpoint documentation checks
- Database schema validation
- Build configuration checks

**Prerequisites:**
- None (runs on any Unix-like system)

**Execution Time:** ~30 seconds

---

### 3. Frontend UI Test Suite (Requires Node.js and Puppeteer)
**File:** `frontend-ui-test.js`
**Purpose:** Automated UI testing with headless browser

```bash
# Install dependencies (first time only)
npm install puppeteer chalk

# Run the UI test suite
node frontend-ui-test.js
```

**This test suite includes:**
- Frontend accessibility testing
- UI element detection
- Button interaction testing
- Form validation testing
- Route navigation testing
- Responsive design testing
- CSS and styling verification
- JavaScript functionality testing
- Accessibility compliance
- Performance metrics

**Prerequisites:**
- Node.js 16+ installed
- Frontend running on http://localhost:3000
- Puppeteer and chalk npm packages

**Execution Time:** ~2-3 minutes

---

## Quick Start - Running All Tests

### Step 1: Environment Setup
```bash
# Ensure .env file exists
cat .env

# Should contain:
# POSTGRES_PASSWORD=your_password
# JWT_SECRET=your_secret_key
```

### Step 2: Run Static Analysis (Quick Check)
```bash
bash static-analysis-test.sh
```

### Step 3: Start Services and Run Full Tests
```bash
# Start all services
docker-compose up -d --build

# Wait for services to be ready (2-3 minutes)
sleep 180

# Run comprehensive tests
bash comprehensive-test-suite.sh
```

### Step 4: Review Results
```bash
# View the latest test report
ls -lt *.md | head -5

# Read the comprehensive report
cat comprehensive-test-report-*.md
```

---

## Test Reports

### Generated Files
After running tests, you'll find:

1. **Test Reports** (Markdown format)
   - `comprehensive-test-report-YYYYMMDD-HHMMSS.md`
   - Detailed analysis and results

2. **Test Logs** (Plain text)
   - `test-results-YYYYMMDD-HHMMSS.log`
   - `static-analysis-YYYYMMDD-HHMMSS.log`
   - Raw test output

3. **Summary Documents**
   - `TESTING_SUMMARY.md` - Overall test summary
   - This guide: `TEST_EXECUTION_GUIDE.md`

---

## Understanding Test Results

### Pass/Fail Criteria
- ✅ **PASS** - Test successful, meets requirements
- ❌ **FAIL** - Test failed, needs attention
- ⚠️ **WARN** - Warning, non-critical issue

### Test Categories
Each test belongs to a category:
1. Project Structure
2. Configuration Files
3. Backend Services
4. Frontend Applications
5. Documentation
6. Code Quality
7. Security
8. API Endpoints
9. Database Schema
10. Build Configuration

### Reading the Summary
```
Total Tests:    172
Passed:         171 (99%)
Failed:         1 (0%)
```

This shows:
- 172 total tests executed
- 171 tests passed
- 1 test failed
- 99% overall pass rate

---

## Troubleshooting

### Issue: Docker not available
**Solution:** Use static analysis test suite only
```bash
bash static-analysis-test.sh
```

### Issue: Tests timing out
**Solution:** Increase wait times in test scripts
```bash
# Edit comprehensive-test-suite.sh
# Find: sleep 10
# Change to: sleep 30
```

### Issue: Port conflicts
**Solution:** Stop conflicting services
```bash
# Check what's using ports
sudo lsof -i :8080
sudo lsof -i :8761

# Stop Docker services and retry
docker-compose down
docker-compose up -d
```

### Issue: Permission denied
**Solution:** Make scripts executable
```bash
chmod +x comprehensive-test-suite.sh
chmod +x static-analysis-test.sh
```

---

## CI/CD Integration

### GitHub Actions Example
```yaml
name: NISIRCOP Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Static Analysis
        run: bash static-analysis-test.sh
      - name: Start Services
        run: docker-compose up -d
      - name: Run Integration Tests
        run: bash comprehensive-test-suite.sh
      - name: Upload Reports
        uses: actions/upload-artifact@v2
        with:
          name: test-reports
          path: "*.md"
```

### GitLab CI Example
```yaml
stages:
  - test

static_analysis:
  stage: test
  script:
    - bash static-analysis-test.sh
  artifacts:
    paths:
      - "*.md"
      - "*.log"

integration_tests:
  stage: test
  script:
    - docker-compose up -d
    - sleep 180
    - bash comprehensive-test-suite.sh
  artifacts:
    paths:
      - "*.md"
      - "*.log"
```

---

## Test Maintenance

### Updating Tests
When you add new features:

1. **Backend Service**
   - Add service directory check to static analysis
   - Add endpoint tests to comprehensive suite

2. **Frontend Component**
   - Add component check to static analysis
   - Add UI tests to frontend suite

3. **API Endpoint**
   - Update API documentation
   - Add endpoint test to comprehensive suite

### Test Script Locations
```
/workspace/
├── comprehensive-test-suite.sh     # Full integration tests
├── static-analysis-test.sh         # Structure validation
├── frontend-ui-test.js             # UI testing
├── TESTING_SUMMARY.md              # This summary
└── TEST_EXECUTION_GUIDE.md         # This guide
```

---

## Best Practices

1. **Run static analysis first** - It's fast and catches configuration issues
2. **Check Docker resources** - Ensure enough RAM/CPU for all services
3. **Wait for services** - Give services time to fully start (2-3 minutes)
4. **Review logs** - Check service logs if tests fail
5. **Keep reports** - Archive test reports for comparison
6. **Run regularly** - Ideally after every code change

---

## Support

For issues or questions:
1. Check the `TESTING_SUMMARY.md` for detailed results
2. Review service logs: `docker-compose logs <service-name>`
3. Check documentation in `/documentation` folder

---

**Last Updated:** October 12, 2025  
**Version:** 1.0
