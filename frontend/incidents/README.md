# NISIRCOP Incidents Module

Incident reporting and management module for the NISIRCOP platform.

## Tech Stack

- **Vue 3.5.22** - Progressive JavaScript framework
- **Vite 7.1.7** - Next generation frontend tooling
- **Pinia 3.0.3** - State management
- **Tailwind CSS 4.1.14** - Utility-first CSS framework
- **PostCSS 8.4.49** - CSS transformation

## Features

- 📝 **Report Incidents**: Create new incident reports with geolocation
- 🗺️ **Interactive Map**: View incidents on an interactive map
- 🔍 **Search & Filter**: Find incidents by date, type, priority
- 📋 **Incident Details**: View complete incident information
- 🎨 **Priority Indicators**: Visual priority level coding (LOW, MEDIUM, HIGH, CRITICAL)

## Development

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build
```

## API Integration

Communicates with Incident Service via API Gateway:
- `GET /api/v1/incidents` - List all incidents
- `POST /api/v1/incidents` - Create new incident
- `GET /api/v1/incidents/{id}` - Get incident details
- `PUT /api/v1/incidents/{id}` - Update incident

## Geospatial Features

Uses Geographic Service for location validation:
- `POST /api/v1/geo/validate` - Validate point within boundaries

## Learn More

See main [PROJECT DOCUMENTATION](/documentation) for complete system details.
