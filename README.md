# 🚀 Users and Authentication Microservice - JASS Digital System

## 📋 Project Purpose

This microservice should provide comprehensive user management and authentication capabilities for the JASS Digital System. It should implement user CRUD operations, JWT authentication, role management, and advanced functionalities. You should use this service to manage all user-related operations and secure your system with JWT-based authentication.

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring WebFlux** (Reactive Programming)
- **MongoDB** (Database)
- **JWT** (Authentication)
- **Spring Security** (Security)
- **Maven** (Dependency Management)

## 🚀 Quick Deployment

### For Windows (PowerShell)

```powershell
# Development mode (without authentication)
.\deploy.ps1

# Production mode (with JWT authentication)
$env:SPRING_PROFILES_ACTIVE="prod"
.\deploy.ps1
```

### For Linux/Mac

```bash
# Development mode (without authentication)
./deploy.sh

# Production mode (with JWT authentication)
export SPRING_PROFILES_ACTIVE=prod
./deploy.sh
```

## 🔐 Security Configuration

### 🟢 Development Mode (`dev`)

- ✅ All endpoints **OPEN**
- ✅ No authentication required
- ✅ Ideal for development and testing

### 🔴 Production Mode (`prod`)

- 🔐 **MANDATORY JWT AUTHENTICATION** for user endpoints
- ✅ Public endpoints: `/api/v1/auth/login`, `/api/v1/health`
- 🛡️ Protected endpoints: **ALL** from `/api/v1/users/**`

## 📚 Main Endpoints

### 🔓 Public (no token required)

```bash
POST /api/v1/auth/login           # Login
GET  /api/v1/health               # Service status
POST /api/v1/auth/reset-password  # Reset password
```

### 🔐 Protected (require token in PROD)

```bash
GET  /api/v1/users/my-profile                    # 🆕 My profile
GET  /api/v1/users/my-profile/with-waterboxes    # 🆕 My profile with water boxes
GET  /api/v1/users/all                           # All users
POST /api/v1/users                               # Create user
PUT  /api/v1/users/{id}                          # Update user
DELETE /api/v1/users/{id}                        # Delete user
```

## 🔑 Usage Examples

### 1. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "Admin123!"}'
```

### 2. Get My Profile

```bash
curl -X GET http://localhost:8080/api/v1/users/my-profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 3. Get My Profile with Water Boxes

```bash
curl -X GET http://localhost:8080/api/v1/users/my-profile/with-waterboxes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## ⚙️ Environment Variables

```bash
# Application profile (dev/prod)
SPRING_PROFILES_ACTIVE=prod

# Server port
SERVER_PORT=8080

# MongoDB URI
MONGO_URI=mongodb+srv://user:pass@cluster.mongodb.net/db

# JWT Configuration
JWT_SECRET=mySecretKey
JWT_EXPIRATION=86400000
```

## 📖 Complete Documentation

Complete API documentation is available at:

- **File**: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
- **Swagger UI**: <http://localhost:8080/swagger-ui.html> (when the app is running)

## 🏗️ Manual Build

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Generate JAR
mvn clean package

# Run JAR
java -jar target/vg-ms-users-authentication-0.0.1-SNAPSHOT.jar
```

## 📂 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── pe/edu/vallegrande/vgmsusersauthentication/
│   │       ├── application/           # Business logic
│   │       │   ├── config/           # Configurations
│   │       │   └── service/          # Services
│   │       ├── domain/               # Entities and enums
│   │       ├── infrastructure/       # Infrastructure
│   │       │   ├── dto/             # DTOs
│   │       │   ├── rest/            # REST Controllers
│   │       │   └── repository/      # Repositories
│   │       └── VgMsUsersAuthenticationApplication.java
│   └── resources/
│       └── application.yml          # Configuration
└── test/                           # Tests
```

## 🆕 New Features

### ✨ Personal Profile Endpoints

- **`GET /api/v1/users/my-profile`**: Get authenticated user information
- **`GET /api/v1/users/my-profile/with-waterboxes`**: Profile with associated water boxes

### 🔐 Enhanced Security

- Mandatory JWT authentication in production for user endpoints
- Automatic security filter based on profiles
- User headers automatically extracted from JWT token

### 🎛️ Flexible Configuration

- Deployment scripts for Windows and Linux
- Environment variables for all parameters
- Configurable profiles (dev/prod)

## 🐛 Troubleshooting

### Error 401 Unauthorized

```json
{
  "success": false,
  "error": {
    "message": "Token no válido o no proporcionado",
    "code": "UNAUTHORIZED",
    "status": 401
  }
}
```

**Solution**: Verify that you are sending the JWT token in the header `Authorization: Bearer TOKEN`

### Compilation error

```bash
# Clean and recompile
mvn clean install -DskipTests
```

### MongoDB connection issues

```bash
# Verify connectivity
curl -X GET http://localhost:8080/api/v1/health
```

## 📞 Support

For technical support or to report bugs, contact the JASS Digital System development team.

---

**Version**: 2.0.0
**Last updated**: June 16, 2025
