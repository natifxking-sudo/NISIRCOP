# NISIRCOP Analytics Module

Crime analytics and reporting module for the NISIRCOP platform.

## Tech Stack

- **Vue 3.5.22** - Progressive JavaScript framework
- **Vite 7.1.7** - Next generation frontend tooling
- **Pinia 3.0.3** - State management
- **Tailwind CSS 4.1.14** - Utility-first CSS framework
- **PostCSS 8.4.49** - CSS transformation

## Features

- 📊 **Crime Trends**: Visualize crime data over time
- 🗺️ **Heat Maps**: Geographic crime density visualization
- 📈 **Statistics**: Incident counts by type, priority, location
- 📅 **Time-based Analysis**: Daily, weekly, monthly trends
- 🎯 **Priority Distribution**: Analyze incident severity patterns

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

Communicates with Analytics Service via API Gateway:
- `GET /api/v1/analytics/trends` - Crime trends data
- `GET /api/v1/analytics/heatmap` - Heat map data
- `GET /api/v1/analytics/stats` - Statistical summaries

## Learn More

See main [PROJECT DOCUMENTATION](/documentation) for complete system details.
