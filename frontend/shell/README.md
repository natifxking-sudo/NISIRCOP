# NISIRCOP Shell Application

The main shell application for the NISIRCOP-LE-ANALYTICS platform - a microfrontend container that hosts all feature modules.

## Tech Stack

- **Vue 3.5.22** - Progressive JavaScript framework
- **Vite 7.1.7** - Next generation frontend tooling
- **Pinia 3.0.3** - State management for Vue 3
- **Tailwind CSS 4.1.14** - Utility-first CSS framework
- **PostCSS 8.4.49** - CSS transformation tool

## Project Structure

```
shell/
├── src/
│   ├── App.vue          # Root component
│   ├── main.js          # Application entry point
│   ├── components/      # Shared components
│   ├── stores/          # Pinia state stores
│   └── assets/          # Static assets
├── public/              # Public static files
├── index.html           # HTML template
├── package.json         # Dependencies
├── vite.config.js       # Vite configuration
└── tailwind.config.js   # Tailwind configuration
```

## Development

### Prerequisites

- Node.js 20 or higher
- npm or yarn

### Install Dependencies

```bash
npm install
```

### Run Development Server

```bash
npm run dev
```

Application will be available at `http://localhost:3000`

### Build for Production

```bash
npm run build
```

Builds the app for production to the `dist/` folder.

### Preview Production Build

```bash
npm run preview
```

## Features

- 🏠 **Shell Container**: Hosts all microfrontend modules
- 🎨 **Modern UI**: Built with Tailwind CSS for responsive design
- 🔐 **Authentication**: JWT-based authentication with API Gateway
- 🗺️ **Navigation**: Seamless routing between modules
- 📦 **State Management**: Centralized state with Pinia
- ⚡ **Fast Dev Experience**: Hot Module Replacement with Vite

## Integration with Backend

The shell application communicates with the backend through the API Gateway at `http://localhost:8080`:

- **Auth**: `POST /auth/login`
- **User Management**: `/api/v1/users/**`
- **Incidents**: `/api/v1/incidents/**`
- **Geographic Data**: `/api/v1/geo/**`
- **Analytics**: `/api/v1/analytics/**`

## Docker

The application is containerized and can be run with Docker:

```bash
# Build image
docker build -t nisircop-shell .

# Run container
docker run -p 3000:80 nisircop-shell
```

Or use the main docker-compose from the root directory.

## IDE Setup

Recommended IDE: [VS Code](https://code.visualstudio.com/) with extensions:
- [Vue Language Features (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.volar)
- [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin)
- [Tailwind CSS IntelliSense](https://marketplace.visualstudio.com/items?itemName=bradlc.vscode-tailwindcss)

## Learn More

- [Vue 3 Documentation](https://vuejs.org/)
- [Vite Documentation](https://vitejs.dev/)
- [Pinia Documentation](https://pinia.vuejs.org/)
- [Tailwind CSS Documentation](https://tailwindcss.com/)
