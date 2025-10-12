# NISIRCOP Users Module

User management module for the NISIRCOP platform.

## Tech Stack

- **Vue 3.5.22** - Progressive JavaScript framework
- **Vite 7.1.7** - Next generation frontend tooling
- **Pinia 3.0.3** - State management
- **Tailwind CSS 4.1.14** - Utility-first CSS framework
- **PostCSS 8.4.49** - CSS transformation

## Features

- 👥 **User Management**: Create, view, update, and delete users
- 🔐 **Role-Based Access**: Support for OFFICER, POLICE_STATION, SUPER_USER roles
- 🏢 **Station Assignment**: Assign users to police station jurisdictions
- 🔍 **User Search**: Find users by username, role, or station
- ✏️ **Profile Management**: Update user information

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

Communicates with User Service via API Gateway:
- `GET /api/v1/users` - List all users (SUPER_USER only)
- `POST /api/v1/users` - Create new user (SUPER_USER, POLICE_STATION)
- `GET /api/v1/users/{id}` - Get user details
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user (SUPER_USER only)

## Role-Based Permissions

- **OFFICER**: Can view their own profile
- **POLICE_STATION**: Can manage officers in their station
- **SUPER_USER**: Full access to all user management features

## Learn More

See main [PROJECT DOCUMENTATION](/documentation) for complete system details.
