# 📚 Documentación Completa de la API - Microservicio de Usuarios y Autenticación

## 📋 Índice de Endpoints

### 🔐 Autenticación (AuthRest)

1. [Iniciar Sesión](#1-iniciar-sesión)
2. [Cerrar Sesión](#2-cerrar-sesión)
3. [Validar Token](#3-validar-token)
4. [Refrescar Token](#4-refrescar-token)
5. [Obtener Token por Usuario](#5-obtener-token-por-usuario)
6. [Cambiar Contraseña](#6-cambiar-contraseña)
7. [Restablecer Contraseña](#7-restablecer-contraseña)
8. [Generar Código de Usuario](#8-generar-código-de-usuario)

### 👥 Gestión de Usuarios (UserRest)

9. [Crear Usuario](#9-crear-usuario)
10. [Obtener Todos los Usuarios](#10-obtener-todos-los-usuarios)
11. [Obtener Usuario por ID](#11-obtener-usuario-por-id)
12. [Obtener Usuario por Email](#12-obtener-usuario-por-email)
13. [Obtener Usuario por Documento](#13-obtener-usuario-por-documento)
14. [Obtener Usuarios por Rol](#14-obtener-usuarios-por-rol)
15. [Obtener Usuarios por Estado](#15-obtener-usuarios-por-estado)
16. [Obtener Usuarios por Organización](#16-obtener-usuarios-por-organización)
17. [Actualizar Usuario Completamente](#17-actualizar-usuario-completamente)
18. [Actualizar Usuario Parcialmente](#18-actualizar-usuario-parcialmente)
19. [Eliminar Usuario](#19-eliminar-usuario)
20. [Restaurar Usuario](#20-restaurar-usuario)
21. [Verificar si Email Existe](#21-verificar-si-email-existe)
22. [Obtener Usuarios con Water Boxes](#22-obtener-usuarios-con-water-boxes)
23. [Obtener Usuario con Water Boxes por ID](#23-obtener-usuario-con-water-boxes-por-id)
24. [Verificar si Documento Existe](#24-verificar-si-documento-existe)

### 🏥 Estado del Servicio (HealthController)

25. [Verificar Estado del Servicio](#25-verificar-estado-del-servicio)

---

## 📊 Información General

- **URL Base**: `http://localhost:8080`
- **Versión API**: `v1`
- **Base Path**: `/api/v1`
- **Formato de Respuesta**: JSON
- **Arquitectura**: Reactiva (WebFlux)

---

## 🔐 Endpoints de Autenticación

### 1. Iniciar Sesión

**Descripción**: Autenticar un usuario y obtener tokens JWT.

**Método**: `POST`
**URL**: `/api/v1/auth/login`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "Admin123!"
  }'
```

#### Cuerpo de la Solicitud

```json
{
  "username": "admin",
  "password": "Admin123!"
}
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": "60b5f0c8e1b2c3d4e5f6g7h8",
      "username": "admin",
      "email": "admin@example.com",
      "roles": ["ADMIN"]
    }
  }
}
```

#### Respuesta de Error (401)

```json
{
  "success": false,
  "error": {
    "message": "Credenciales inválidas",
    "code": "LOGIN_FAILED",
    "status": 401
  }
}
```

---

### 2. Cerrar Sesión

**Descripción**: Cerrar sesión de un usuario específico.

**Método**: `POST`
**URL**: `/api/v1/auth/logout/{userId}`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": "Logout exitoso"
}
```

#### Respuesta de Error (400)

```json
{
  "success": false,
  "error": {
    "message": "Usuario no encontrado",
    "code": "LOGOUT_FAILED",
    "status": 400
  }
}
```

---

### 3. Validar Token

**Descripción**: Validar un token JWT.

**Método**: `POST`
**URL**: `/api/v1/auth/validate?token={token}`

#### Solicitud

```bash
curl -X POST "http://localhost:8080/api/v1/auth/validate?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "valid": true,
    "userId": "60b5f0c8e1b2c3d4e5f6g7h8",
    "username": "admin",
    "roles": ["ADMIN"],
    "expiresAt": "2025-06-17T10:30:00Z"
  }
}
```

#### Respuesta de Error (401)

```json
{
  "success": false,
  "error": {
    "message": "Token inválido o expirado",
    "code": "TOKEN_VALIDATION_FAILED",
    "status": 401
  }
}
```

---

### 4. Refrescar Token

**Descripción**: Obtener un nuevo token usando el refresh token.

**Método**: `POST`
**URL**: `/api/v1/auth/refresh`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }'
```

#### Cuerpo de la Solicitud

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

---

### 5. Obtener Token por Usuario

**Descripción**: Obtener el token de un usuario específico.

**Método**: `GET`
**URL**: `/api/v1/auth/token/user/{userId}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/auth/token/user/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

---

### 6. Cambiar Contraseña

**Descripción**: Cambiar la contraseña de un usuario.

**Método**: `POST`
**URL**: `/api/v1/auth/change-password/{userId}`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/auth/change-password/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "currentPassword": "OldPassword123!",
    "newPassword": "NewPassword123!"
  }'
```

#### Cuerpo de la Solicitud

```json
{
  "currentPassword": "OldPassword123!",
  "newPassword": "NewPassword123!"
}
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": "Contraseña cambiada exitosamente"
}
```

---

### 7. Restablecer Contraseña

**Descripción**: Enviar una contraseña temporal por email.

**Método**: `POST`
**URL**: `/api/v1/auth/reset-password?email={email}`

#### Solicitud

```bash
curl -X POST "http://localhost:8080/api/v1/auth/reset-password?email=user@example.com"
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": "Contraseña temporal enviada al email"
}
```

---

### 8. Generar Código de Usuario

**Descripción**: Generar un código único para un usuario en una organización.

**Método**: `GET`
**URL**: `/api/v1/auth/generate-user-code/{organizationId}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/auth/generate-user-code/ORG001 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": "USR-ORG001-001"
}
```

---

## 👥 Endpoints de Gestión de Usuarios

### 9. Crear Usuario

**Descripción**: Crear un nuevo usuario en el sistema.

**Método**: `POST`
**URL**: `/api/v1/users`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "organizationId": "ORG001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@example.com",
    "phone": "+51987654321",
    "streetAddress": "Av. Principal 123",
    "streetId": "ST001",
    "zoneId": "ZN001",
    "username": "jperez",
    "password": "SecurePass123!",
    "roles": ["USER"]
  }'
```

#### Cuerpo de la Solicitud

```json
{
  "organizationId": "ORG001",
  "documentType": "DNI",
  "documentNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@example.com",
  "phone": "+51987654321",
  "streetAddress": "Av. Principal 123",
  "streetId": "ST001",
  "zoneId": "ZN001",
  "username": "jperez",
  "password": "SecurePass123!",
  "roles": ["USER"]
}
```

#### Respuesta de Éxito (201)

```json
{
  "success": true,
  "data": {
    "id": "60b5f0c8e1b2c3d4e5f6g7h8",
    "organizationId": "ORG001",
    "userCode": "USR-ORG001-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "contact": {
      "email": "juan.perez@example.com",
      "phone": "+51987654321"
    },
    "address": {
      "streetAddress": "Av. Principal 123",
      "streetId": "ST001",
      "zoneId": "ZN001"
    },
    "status": "ACTIVE",
    "roles": ["USER"],
    "createdAt": "2025-06-16T10:30:00Z",
    "updatedAt": "2025-06-16T10:30:00Z"
  }
}
```

---

### 10. Obtener Todos los Usuarios

**Descripción**: Obtener lista de todos los usuarios.

**Método**: `GET`
**URL**: `/api/v1/users/all`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/all \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "status": true,
  "data": [
    {
      "id": "684fc2affb12121a909bd2d1",
      "userCode": "USR-00001",
      "organizationId": "1",
      "documentType": "DNI",
      "documentNumber": "770044149",
      "firstName": "Isael Javier",
      "lastName": "Fatama Godoy",
      "fullName": "Isael Javier Fatama Godoy",
      "email": "isael.fatamav2@vallegrande.edu.pe",
      "phone": "922143355",
      "streetAddress": "Rinconada de Conta s/n",
      "streetId": "1",
      "zoneId": "1",
      "status": "ACTIVE",
      "registrationDate": "2025-06-16T02:07:27.466",
      "lastLogin": "2025-06-16T02:08:10.582",
      "createdAt": "2025-06-16T02:07:27.466",
      "updatedAt": "2025-06-16T02:07:27.466",
      "roles": [
        "CLIENT"
      ],
      "username": "IsaelFatamaDevv2"
    }
  ]
}
```

---

### 11. Obtener Usuario por ID

**Descripción**: Obtener información de un usuario específico por su ID.

**Método**: `GET`
**URL**: `/api/v1/users/{userId}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "id": "60b5f0c8e1b2c3d4e5f6g7h8",
    "organizationId": "ORG001",
    "userCode": "USR-ORG001-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "contact": {
      "email": "juan.perez@example.com",
      "phone": "+51987654321"
    },
    "address": {
      "streetAddress": "Av. Principal 123",
      "streetId": "ST001",
      "zoneId": "ZN001"
    },
    "status": "ACTIVE",
    "roles": ["USER"],
    "createdAt": "2025-06-16T10:30:00Z",
    "updatedAt": "2025-06-16T10:30:00Z"
  }
}
```

---

### 12. Obtener Usuario por Email

**Descripción**: Buscar un usuario por su dirección de email.

**Método**: `GET`
**URL**: `/api/v1/users/email/{email}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/email/juan.perez@example.com \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta: Similar a la respuesta del endpoint anterior

---

### 13. Obtener Usuario por Documento

**Descripción**: Buscar un usuario por su número de documento.

**Método**: `GET`
**URL**: `/api/v1/users/document/{documentNumber}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/document/12345678 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 14. Obtener Usuarios por Rol

**Descripción**: Filtrar usuarios por rol específico.

**Método**: `GET`
**URL**: `/api/v1/users/role/{role}`

**Roles disponibles**: `ADMIN`, `USER`, `OPERATOR`, `VIEWER`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/role/ADMIN \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": [
    {
      "id": "60b5f0c8e1b2c3d4e5f6g7h8",
      "firstName": "Admin",
      "lastName": "User",
      "roles": ["ADMIN"],
      "status": "ACTIVE"
    }
  ]
}
```

---

### 15. Obtener Usuarios por Estado

**Descripción**: Filtrar usuarios por estado.

**Método**: `GET`
**URL**: `/api/v1/users/status/{status}`

**Estados disponibles**: `ACTIVE`, `INACTIVE`, `SUSPENDED`, `PENDING`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/status/ACTIVE \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 16. Obtener Usuarios por Organización

**Descripción**: Obtener todos los usuarios de una organización específica.

**Método**: `GET`
**URL**: `/api/v1/users/organization/{organizationId}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/organization/ORG001 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 17. Actualizar Usuario Completamente

**Descripción**: Actualizar todos los campos de un usuario (PUT).

**Método**: `PUT`
**URL**: `/api/v1/users/{userId}`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X PUT http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "organizationId": "ORG001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan Carlos",
    "lastName": "Pérez García",
    "email": "juan.perez@example.com",
    "phone": "+51987654321",
    "streetAddress": "Av. Nueva 456",
    "streetId": "ST002",
    "zoneId": "ZN002",
    "username": "jcperez",
    "password": "NewSecurePass123!",
    "roles": ["USER", "OPERATOR"]
  }'
```

---

### 18. Actualizar Usuario Parcialmente

**Descripción**: Actualizar campos específicos de un usuario (PATCH).

**Método**: `PATCH`
**URL**: `/api/v1/users/{userId}`
**Content-Type**: `application/json`

#### Solicitud

```bash
curl -X PATCH http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "firstName": "Juan Carlos",
    "phone": "+51999888777",
    "streetAddress": "Nueva Dirección 789"
  }'
```

---

### 19. Eliminar Usuario

**Descripción**: Eliminar lógicamente un usuario (cambiar estado a inactivo).

**Método**: `DELETE`
**URL**: `/api/v1/users/{userId}`

#### Solicitud

```bash
curl -X DELETE http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": "Usuario eliminado correctamente"
}
```

---

### 20. Restaurar Usuario

**Descripción**: Restaurar un usuario eliminado lógicamente.

**Método**: `POST`
**URL**: `/api/v1/users/{userId}/restore`

#### Solicitud

```bash
curl -X POST http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8/restore \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 21. Verificar si Email Existe

**Descripción**: Verificar si un email ya está registrado en el sistema.

**Método**: `GET`
**URL**: `/api/v1/users/exists/email/{email}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/exists/email/juan.perez@example.com \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": true
}
```

---

### 22. Obtener Usuarios con Water Boxes

**Descripción**: Obtener todos los usuarios con información de sus water boxes asociados.

**Método**: `GET`
**URL**: `/api/v1/users/with-waterboxes`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/with-waterboxes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": [
    {
      "id": "60b5f0c8e1b2c3d4e5f6g7h8",
      "firstName": "Juan",
      "lastName": "Pérez",
      "email": "juan.perez@example.com",
      "waterBoxes": [
        {
          "id": "wb001",
          "serialNumber": "WB-001-2025",
          "status": "ACTIVE",
          "installationDate": "2025-01-15T08:00:00Z"
        }
      ],
      "totalWaterBoxes": 1
    }
  ]
}
```

---

### 23. Obtener Usuario con Water Boxes por ID

**Descripción**: Obtener un usuario específico con información de sus water boxes.

**Método**: `GET`
**URL**: `/api/v1/users/{userId}/with-waterboxes`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/60b5f0c8e1b2c3d4e5f6g7h8/with-waterboxes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

### 24. Verificar si Documento Existe

**Descripción**: Verificar si un número de documento ya está registrado.

**Método**: `GET`
**URL**: `/api/v1/users/exists/document/{documentNumber}`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/exists/document/12345678 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": false
}
```

---

### 25. Obtener Mi Perfil

**Descripción**: Obtener la información del perfil del usuario autenticado.

**Método**: `GET`
**URL**: `/api/v1/users/my-profile`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/my-profile \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "id": "60b5f0c8e1b2c3d4e5f6g7h8",
    "organizationId": "ORG001",
    "userCode": "USR-ORG001-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "contact": {
      "email": "juan.perez@example.com",
      "phone": "+51987654321"
    },
    "address": {
      "streetAddress": "Av. Principal 123",
      "streetId": "ST001",
      "zoneId": "ZN001"
    },
    "status": "ACTIVE",
    "roles": ["USER"],
    "createdAt": "2025-06-16T10:30:00Z",
    "updatedAt": "2025-06-16T10:30:00Z"
  }
}
```

---

### 26. Obtener Mi Perfil con Water Boxes

**Descripción**: Obtener la información del perfil del usuario autenticado incluyendo sus water boxes.

**Método**: `GET`
**URL**: `/api/v1/users/my-profile/with-waterboxes`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/users/my-profile/with-waterboxes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Respuesta de Éxito (200)

```json
{
  "success": true,
  "data": {
    "id": "60b5f0c8e1b2c3d4e5f6g7h8",
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@example.com",
    "waterBoxes": [
      {
        "id": "wb001",
        "serialNumber": "WB-001-2025",
        "status": "ACTIVE",
        "installationDate": "2025-01-15T08:00:00Z"
      }
    ],
    "totalWaterBoxes": 1
  }
}
```

---

## 🏥 Endpoint de Estado del Servicio

### 27. Verificar Estado del Servicio

**Descripción**: Verificar que el microservicio esté funcionando correctamente.

**Método**: `GET`
**URL**: `/api/v1/health`

#### Solicitud

```bash
curl -X GET http://localhost:8080/api/v1/health
```

#### Respuesta de Éxito (200)

```json
{
  "status": "UP",
  "timestamp": "2025-06-16T10:30:00Z",
  "service": "vg-ms-users-authentication",
  "version": "2.0.0",
  "message": "Microservicio de Usuarios y Autenticación funcionando correctamente"
}
```

---

## 📝 Notas Importantes

### Autenticación

- La mayoría de endpoints requieren autenticación JWT
- Incluir el token en el header: `Authorization: Bearer {token}`
- Los tokens expiran en 24 horas por defecto
- Usar el endpoint de refresh para renovar tokens

### Códigos de Estado HTTP

- **200**: Operación exitosa
- **201**: Recurso creado exitosamente
- **400**: Error en la solicitud (datos inválidos)
- **401**: No autorizado (token inválido/expirado)
- **403**: Prohibido (sin permisos)
- **404**: Recurso no encontrado
- **409**: Conflicto (recurso ya existe)
- **500**: Error interno del servidor

### Formato de Respuesta

Todas las respuestas siguen el formato estándar:

```json
{
  "success": boolean,
  "data": object|array|string,
  "error": {
    "message": "string",
    "code": "string",
    "status": number
  }
}
```

### Tipos de Documento

- `DNI`: Documento Nacional de Identidad
- `PASSPORT`: Pasaporte
- `CE`: Carné de Extranjería

### Roles de Usuario

- `ADMIN`: Administrador del sistema
- `USER`: Usuario regular
- `OPERATOR`: Operador del sistema
- `VIEWER`: Solo lectura

### Estados de Usuario

- `ACTIVE`: Usuario activo
- `INACTIVE`: Usuario inactivo
- `SUSPENDED`: Usuario suspendido
- `PENDING`: Usuario pendiente de activación

---

## 🚀 Ejemplo de Flujo Completo

### 1. Autenticación

```bash
# Iniciar sesión
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "Admin123!"}'
```

### 2. Crear Usuario

```bash
# Crear nuevo usuario
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{...user_data...}'
```

### 3. Consultar Usuario

```bash
# Obtener usuario por ID
curl -X GET http://localhost:8080/api/v1/users/{userId} \
  -H "Authorization: Bearer {token}"
```

### 4. Verificar Estado

```bash
# Verificar que el servicio esté funcionando
curl -X GET http://localhost:8080/api/v1/health
```

---

*Documentación generada para el Microservicio de Usuarios y Autenticación - Sistema JASS Digital v2.0.0*

- **Spring WebFlux** (Programación reactiva)
- **MongoDB Reactive**
- **JWT (JSON Web Tokens)**
- **Spring Security**
- **Maven**
- **Lombok**
- **CORS configurado**
- **Swagger/OpenAPI 3**

## Configuración

### Variables de Entorno

```bash
# MongoDB
MONGO_USERNAME=sistemajass
MONGO_PASSWORD=ZC7O1Ok40SwkfEje
MONGO_DATABASE=JASS_DIGITAL

# JWT
JWT_SECRET=mySecretKeyForJASSDigitalSystemUsersAuthenticationMicroservice2024
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
JWT_ISSUER=JASS-Digital-System

# Servidor
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

## API Endpoints

### 🏥 Health Check (`/api/v1/health`)

#### GET /api/v1/health

Verificar el estado de salud del microservicio

**Respuesta:**

```json
{
  "status": "UP",
  "timestamp": "2024-06-16T10:30:00",
  "service": "vg-ms-users-authentication",
  "version": "2.0.0",
  "message": "Microservicio de Usuarios y Autenticación funcionando correctamente"
}
```

### 🔐 Autenticación (`/api/v1/auth`)

#### POST /api/v1/auth/login

Iniciar sesión con credenciales

**Request Body:**

```json
{
  "username": "admin123",
  "password": "Admin123@"
}
```

**Response 200:**

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400000,
    "user": {
      "id": "60f7b3b3b3b3b3b3b3b3b3b3",
      "userCode": "USR-00001",
      "email": "admin@jass.com",
      "firstName": "Admin",
      "lastName": "Sistema",
      "roles": ["ADMIN"]
    }
  }
}
```

#### POST /api/v1/auth/logout/{userId}

Cerrar sesión de un usuario específico

**Response 200:**

```json
{
  "success": true,
  "data": "Logout exitoso"
}
```

#### POST /api/v1/auth/validate

Validar token JWT

**Request Body:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response 200:**

```json
{
  "success": true,
  "data": {
    "valid": true,
    "userId": "60f7b3b3b3b3b3b3b3b3b3b3",
    "username": "admin123",
    "roles": ["ADMIN"],
    "expiresAt": "2024-06-17T10:30:00"
  }
}
```

#### POST /api/v1/auth/refresh

Renovar token JWT

**Request Body:**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response 200:**

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400000
  }
}
```

#### GET /api/v1/auth/token/user/{userId}

Obtener información del token por ID de usuario

**Response 200:**

```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "valid": true,
    "expiresAt": "2024-06-17T10:30:00"
  }
}
```

#### POST /api/v1/auth/change-password/{userId}

Cambiar contraseña de un usuario

**Request Body:**

```json
{
  "currentPassword": "Admin123@",
  "newPassword": "NewAdmin123@",
  "confirmPassword": "NewAdmin123@"
}
```

**Response 200:**

```json
{
  "success": true,
  "data": "Contraseña actualizada exitosamente"
}
```

#### POST /api/v1/auth/reset-password

Restablecer contraseña (envío de email)

**Request Body:**

```json
{
  "email": "usuario@jass.com"
}
```

**Response 200:**

```json
{
  "success": true,
  "data": "Instrucciones de restablecimiento enviadas al email"
}
```

#### GET /api/v1/auth/generate-user-code/{organizationId}

Generar código de usuario único para una organización

**Response 200:**

```json
{
  "success": true,
  "data": "USR-00015"
}
```

### 👥 Gestión de Usuarios (`/api/v1/users`)

#### POST /api/v1/users

Crear nuevo usuario

**Request Body:**

```json
{
  "organizationId": "org-001",
  "documentType": "DNI",
  "documentNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez García",
  "email": "juan.perez@jass.com",
  "phone": "987654321",
  "streetAddress": "Av. Los Incas 123",
  "streetId": "street-001",
  "zoneId": "zone-001",
  "roles": ["USER"],
  "username": "jperez",
  "password": "User123@"
}
```

**Response 201:**

```json
{
  "success": true,
  "data": {
    "id": "60f7b3b3b3b3b3b3b3b3b3b3",
    "userCode": "USR-00002",
    "organizationId": "org-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez García",
    "fullName": "Juan Pérez García",
    "email": "juan.perez@jass.com",
    "phone": "987654321",
    "streetAddress": "Av. Los Incas 123",
    "streetId": "street-001",
    "zoneId": "zone-001",
    "status": "ACTIVE",
    "registrationDate": "2024-06-16T10:30:00",
    "createdAt": "2024-06-16T10:30:00",
    "updatedAt": "2024-06-16T10:30:00",
    "roles": ["USER"],
    "username": "jperez"
  }
}
```

#### GET /api/v1/users/all

Obtener todos los usuarios

**Response 200:**

```json
{
  "success": true,
  "data": [
    {
      "id": "60f7b3b3b3b3b3b3b3b3b3b3",
      "userCode": "USR-00001",
      "organizationId": "org-001",
      "documentType": "DNI",
      "documentNumber": "12345678",
      "firstName": "Juan",
      "lastName": "Pérez García",
      "fullName": "Juan Pérez García",
      "email": "juan.perez@jass.com",
      "phone": "987654321",
      "streetAddress": "Av. Los Incas 123",
      "streetId": "street-001",
      "zoneId": "zone-001",
      "status": "ACTIVE",
      "registrationDate": "2024-06-16T10:30:00",
      "lastLogin": "2024-06-16T10:30:00",
      "createdAt": "2024-06-16T10:30:00",
      "updatedAt": "2024-06-16T10:30:00",
      "roles": ["USER"],
      "username": "jperez"
    }
  ]
}
```

#### GET /api/v1/users/{userId}

Obtener usuario por ID

**Response 200:**

```json
{
  "success": true,
  "data": {
    "id": "60f7b3b3b3b3b3b3b3b3b3b3",
    "userCode": "USR-00001",
    "organizationId": "org-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez García",
    "fullName": "Juan Pérez García",
    "email": "juan.perez@jass.com",
    "phone": "987654321",
    "streetAddress": "Av. Los Incas 123",
    "streetId": "street-001",
    "zoneId": "zone-001",
    "status": "ACTIVE",
    "registrationDate": "2024-06-16T10:30:00",
    "lastLogin": "2024-06-16T10:30:00",
    "createdAt": "2024-06-16T10:30:00",
    "updatedAt": "2024-06-16T10:30:00",
    "roles": ["USER"],
    "username": "jperez"
  }
}
```

#### GET /api/v1/users/email/{email}

Obtener usuario por email

**Response 200:** Mismo formato que obtener por ID

#### GET /api/v1/users/document/{documentNumber}

Obtener usuario por número de documento

**Response 200:** Mismo formato que obtener por ID

#### GET /api/v1/users/role/{role}

Obtener usuarios por rol

**Parámetros:** role = ADMIN | USER | OPERATOR | SECRETARY

**Response 200:** Lista de usuarios con el rol especificado

#### GET /api/v1/users/status/{status}

Obtener usuarios por estado

**Parámetros:** status = ACTIVE | INACTIVE | SUSPENDED | PENDING

**Response 200:** Lista de usuarios con el estado especificado

#### GET /api/v1/users/organization/{organizationId}

Obtener usuarios por organización

**Response 200:** Lista de usuarios de la organización especificada

#### PUT /api/v1/users/{userId}

Actualizar usuario completamente

**Request Body:** Mismo formato que crear usuario

**Response 200:** Usuario actualizado

#### PATCH /api/v1/users/{userId}

Actualizar usuario parcialmente

**Request Body:**

```json
{
  "firstName": "Juan Carlos",
  "phone": "999888777",
  "streetAddress": "Nueva Dirección 456"
}
```

**Response 200:** Usuario actualizado

#### DELETE /api/v1/users/{userId}

Eliminar usuario lógicamente

**Response 200:**

```json
{
  "success": true,
  "data": {
    "id": "60f7b3b3b3b3b3b3b3b3b3b3",
    "status": "INACTIVE",
    "message": "Usuario eliminado lógicamente"
  }
}
```

#### POST /api/v1/users/{userId}/restore

Restaurar usuario eliminado lógicamente

**Response 200:**

```json
{
  "success": true,
  "data": {
    "id": "60f7b3b3b3b3b3b3b3b3b3b3",
    "status": "ACTIVE",
    "message": "Usuario restaurado exitosamente"
  }
}
```

#### GET /api/v1/users/exists/email/{email}

Verificar si existe un email

**Response 200:**

```json
{
  "success": true,
  "data": true
}
```

#### GET /api/v1/users/exists/document/{documentNumber}

Verificar si existe un número de documento

**Response 200:**

```json
{
  "success": true,
  "data": false
}
```

### 💧 Usuarios con WaterBoxes (`/api/v1/users/with-waterboxes`)

#### GET /api/v1/users/with-waterboxes

Obtener todos los usuarios con información de waterboxes simulados

**Response 200:**

```json
{
  "success": true,
  "data": [
    {
      "id": "60f7b3b3b3b3b3b3b3b3b3b3",
      "userCode": "USR-00001",
      "organizationId": "org-001",
      "documentType": "DNI",
      "documentNumber": "12345678",
      "firstName": "Juan",
      "lastName": "Pérez García",
      "fullName": "Juan Pérez García",
      "email": "juan.perez@jass.com",
      "phone": "987654321",
      "streetAddress": "Av. Los Incas 123",
      "streetId": "street-001",
      "zoneId": "zone-001",
      "status": "ACTIVE",
      "registrationDate": "2024-06-16T10:30:00",
      "lastLogin": "2024-06-16T10:30:00",
      "createdAt": "2024-06-16T10:30:00",
      "updatedAt": "2024-06-16T10:30:00",
      "roles": ["USER"],
      "username": "jperez",
      "waterBoxes": [
        {
          "waterBoxId": "wb-001",
          "waterBoxCode": "WB-00001",
          "waterBoxName": "Casa Principal",
          "location": "Av. Los Incas 123",
          "assignmentStatus": "ACTIVE",
          "monthlyFee": 25.50,
          "assignmentDate": "2024-01-15T00:00:00",
          "lastReading": "2024-06-15T08:30:00",
          "currentConsumption": 145.75,
          "meterNumber": "MTR-001234"
        }
      ]
    }
  ]
}
```

#### GET /api/v1/users/{userId}/with-waterboxes

Obtener usuario específico con información de waterboxes simulados

**Response 200:**

```json
{
  "success": true,
  "data": {
    "id": "60f7b3b3b3b3b3b3b3b3b3b3",
    "userCode": "USR-00001",
    "organizationId": "org-001",
    "documentType": "DNI",
    "documentNumber": "12345678",
    "firstName": "Juan",
    "lastName": "Pérez García",
    "fullName": "Juan Pérez García",
    "email": "juan.perez@jass.com",
    "phone": "987654321",
    "streetAddress": "Av. Los Incas 123",
    "streetId": "street-001",
    "zoneId": "zone-001",
    "status": "ACTIVE",
    "registrationDate": "2024-06-16T10:30:00",
    "lastLogin": "2024-06-16T10:30:00",
    "createdAt": "2024-06-16T10:30:00",
    "updatedAt": "2024-06-16T10:30:00",
    "roles": ["USER"],
    "username": "jperez",
    "waterBoxes": [
      {
        "waterBoxId": "wb-001",
        "waterBoxCode": "WB-00001",
        "waterBoxName": "Casa Principal",
        "location": "Av. Los Incas 123",
        "assignmentStatus": "ACTIVE",
        "monthlyFee": 25.50,
        "assignmentDate": "2024-01-15T00:00:00",
        "lastReading": "2024-06-15T08:30:00",
        "currentConsumption": 145.75,
        "meterNumber": "MTR-001234"
      },
      {
        "waterBoxId": "wb-002",
        "waterBoxCode": "WB-00002",
        "waterBoxName": "Jardín Posterior",
        "location": "Av. Los Incas 123 - Jardín",
        "assignmentStatus": "ACTIVE",
        "monthlyFee": 15.00,
        "assignmentDate": "2024-02-01T00:00:00",
        "lastReading": "2024-06-15T08:35:00",
        "currentConsumption": 87.25,
        "meterNumber": "MTR-001235"
      }
    ]
  }
}
```

## Estructura de Datos

### UserResponse

```json
{
  "id": "string",
  "userCode": "string",
  "organizationId": "string",
  "documentType": "DNI | PASSPORT | RUC | OTHER",
  "documentNumber": "string",
  "firstName": "string",
  "lastName": "string",
  "fullName": "string",
  "email": "string",
  "phone": "string",
  "streetAddress": "string",
  "streetId": "string",
  "zoneId": "string",
  "status": "ACTIVE | INACTIVE | SUSPENDED | PENDING",
  "registrationDate": "datetime",
  "lastLogin": "datetime",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "roles": ["ADMIN | USER | OPERATOR | SECRETARY"],
  "username": "string"
}
```

### WaterBoxInfo

```json
{
  "waterBoxId": "string",
  "waterBoxCode": "string",
  "waterBoxName": "string",
  "location": "string",
  "assignmentStatus": "ACTIVE | INACTIVE | SUSPENDED | MAINTENANCE",
  "monthlyFee": "decimal",
  "assignmentDate": "datetime",
  "lastReading": "datetime",
  "currentConsumption": "decimal",
  "meterNumber": "string"
}
```

### AuthResponse

```json
{
  "token": "string",
  "refreshToken": "string",
  "expiresIn": "number",
  "user": "UserResponse"
}
```

### ErrorResponse

```json
{
  "success": false,
  "error": {
    "message": "string",
    "code": "string",
    "status": "number"
  }
}
```

## Códigos de Error

### Autenticación

- `LOGIN_FAILED` (401): Credenciales inválidas
- `TOKEN_EXPIRED` (401): Token JWT expirado
- `TOKEN_INVALID` (401): Token JWT inválido
- `UNAUTHORIZED` (401): Sin autorización
- `FORBIDDEN` (403): Acceso denegado

### Usuarios

- `USER_NOT_FOUND` (404): Usuario no encontrado
- `USER_CREATION_ERROR` (400): Error al crear usuario
- `USER_UPDATE_ERROR` (400): Error al actualizar usuario
- `USER_DELETE_ERROR` (400): Error al eliminar usuario
- `USER_RESTORE_ERROR` (400): Error al restaurar usuario
- `EMAIL_ALREADY_EXISTS` (409): Email ya existe
- `DOCUMENT_ALREADY_EXISTS` (409): Documento ya existe
- `USERS_WITH_WATERBOXES_ERROR` (500): Error obteniendo usuarios con waterboxes
- `USER_WITH_WATERBOXES_NOT_FOUND` (404): Usuario con waterboxes no encontrado

### Validación

- `VALIDATION_ERROR` (400): Error de validación de datos
- `INVALID_REQUEST` (400): Solicitud inválida
- `MISSING_REQUIRED_FIELD` (400): Campo requerido faltante

## Notas Importantes

1. **Programación Reactiva**: Todas las respuestas son reactivas usando Mono/Flux
2. **CORS**: Configurado para desarrollo con origen `http://localhost:5173`
3. **JWT**: Tokens con expiración configurable y refresh tokens
4. **Validaciones**: Todas las entradas son validadas usando Bean Validation
5. **Mensajes**: Todos los mensajes de error están en español
6. **WaterBoxes**: Los datos de waterboxes son simulados hasta la integración con el microservicio correspondiente
7. **Seguridad**: Endpoints protegidos con JWT excepto login y health check
8. **Documentación**: Swagger UI disponible en `/swagger-ui.html`

## URLs de Desarrollo

- **API Base URL**: `http://localhost:8080/api/v1`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Health Check**: `http://localhost:8080/api/v1/health`

```

**Respuesta:**

```json
{
  "status": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "userId": "507f1f77bcf86cd799439011",
    "username": "admin123",
    "fullName": "Juan Pérez",
    "roles": ["ADMIN"]
  }
}
```

#### POST /api/v2/auth/logout/{userId}

Cerrar sesión

#### POST /api/v2/auth/validate?token={token}

Validar token JWT

#### POST /api/v2/auth/refresh

Refrescar token

```json
{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

#### GET /api/v2/auth/token/user/{userId}

Obtener token por ID de usuario

#### POST /api/v2/auth/change-password/{userId}

Cambiar contraseña

```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "NewPassword123@",
  "confirmPassword": "NewPassword123@"
}
```

#### POST /api/v2/auth/reset-password?email={email}

Restablecer contraseña

### 👥 Usuarios (api/v2/users)

#### POST /api/v2/users

Crear nuevo usuario

```json
{
  "organizationId": "ORG001",
  "documentType": "DNI",
  "documentNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@example.com",
  "phone": "+51987654321",
  "address": "Av. Principal 123",
  "city": "Lima",
  "state": "Lima",
  "zipCode": "15001",
  "country": "Perú",
  "username": "jperez123",
  "password": "Password123@",
  "roles": ["CLIENT"]
}
```

#### GET /api/v2/users

Obtener todos los usuarios

#### GET /api/v2/users/{userId}

Obtener usuario por ID

#### GET /api/v2/users/email/{email}

Obtener usuario por email

#### GET /api/v2/users/document/{documentNumber}

Obtener usuario por número de documento

#### GET /api/v2/users/role/{role}

Obtener usuarios por rol (CLIENT, ADMIN, SUPER_ADMIN)

#### GET /api/v2/users/status/{status}

Obtener usuarios por estado (ACTIVE, INACTIVE, DELETED)

#### GET /api/v2/users/organization/{organizationId}

Obtener usuarios por organización

#### PUT /api/v2/users/{userId}

Actualizar usuario completamente

#### PATCH /api/v2/users/{userId}

Actualizar usuario parcialmente

```json
{
  "firstName": "Juan Carlos",
  "phone": "+51998877665"
}
```

#### DELETE /api/v2/users/{userId}

Eliminar usuario lógicamente

#### POST /api/v2/users/{userId}/restore

Restaurar usuario eliminado

#### GET /api/v2/users/exists/email/{email}

Verificar si existe email

#### GET /api/v2/users/exists/document/{documentNumber}

Verificar si existe documento

## Modelos de Datos

### Usuario

```json
{
  "id": "507f1f77bcf86cd799439011",
  "userCode": "USR-ORG001-A1B2C3D4",
  "organizationId": "ORG001",
  "documentType": "DNI",
  "documentNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez",
  "fullName": "Juan Pérez",
  "email": "juan.perez@example.com",
  "phone": "+51987654321",
  "address": "Av. Principal 123",
  "city": "Lima",
  "state": "Lima",
  "zipCode": "15001",
  "country": "Perú",
  "status": "ACTIVE",
  "registrationDate": "2024-01-15T10:30:00",
  "lastLogin": "2024-01-20T09:15:00",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-20T09:15:00",
  "roles": ["CLIENT"],
  "username": "jperez123"
}
```

### Respuesta Estándar

```json
{
  "status": true,
  "data": { ... },
  "error": {
    "message": "Error message",
    "errorCode": "ERROR_CODE",
    "httpStatus": 400
  }
}
```

## Enums

### Tipos de Documento

- `DNI`: Documento Nacional de Identidad
- `CARNET_EXTRANJERIA`: Carnet de Extranjería

### Roles de Usuario

- `CLIENT`: Cliente básico
- `ADMIN`: Administrador
- `SUPER_ADMIN`: Super administrador

### Estados de Usuario

- `ACTIVE`: Usuario activo
- `INACTIVE`: Usuario inactivo
- `DELETED`: Usuario eliminado

### Estados de Asignación

- `ASSIGNED`: Asignado
- `PENDING`: Pendiente
- `CANCELLED`: Cancelado

## Swagger UI

Una vez que el servicio esté corriendo, puedes acceder a la documentación interactiva en:

- **URL**: <http://localhost:8080/swagger-ui.html>

## Health Check

- **URL**: <http://localhost:8080/actuator/health>

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── pe/edu/vallegrande/vgmsusersauthentication/
│   │       ├── application/
│   │       │   ├── config/         # Configuraciones
│   │       │   └── service/        # Servicios de aplicación
│   │       ├── domain/
│   │       │   ├── enums/          # Enumeraciones
│   │       │   ├── model/          # Modelos de dominio
│   │       │   └── services/       # Servicios de dominio
│   │       └── infrastructure/
│   │           ├── dto/            # Data Transfer Objects
│   │           ├── exception/      # Manejo de excepciones
│   │           ├── repository/     # Repositorios
│   │           ├── rest/           # Controladores REST
│   │           └── service/        # Servicios de infraestructura
│   └── resources/
│       └── application.yml         # Configuración
└── test/                          # Pruebas unitarias
```
