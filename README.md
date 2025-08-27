# Configuración del Backend para BTG Fondos App

Este proyecto es un backend de ejemplo desarrollado en **Spring Boot** que permite a los usuarios:

- Suscribirse a fondos de inversión.
- Cancelar fondos.
- Consultar el historial de transacciones.

Se conecta a **MongoDB** para persistir la información.

---

## 🔧 Requisitos

- Java 21
- Maven 3.8+
- MongoDB (Atlas o local)
- Git

---

## Configuración de MongoDB

1. Crear un cluster en MongoDB Atlas (o localmente).
2. Crear una base de datos, en este caso la llame ClusterPruebaTecnica.
3. Crear las colecciones necesarias (en este caso):

- `usuarios`
- `fondos`
- `transacciones`

4. Como es un ejercicio de prueba deje la cadena de conexion de la base de datos que cree en `application.properties`
   
## Modelos de Datos
- `usuarios`
```typescript
{
  "_id": "1",
  "nombre": "Juan",
  "saldo": 1000.0,
  "fondosSuscritos": ["101", "102"],
  "preferencia": "Email",
  "email": "juan@mail.com",
  "telefono": "3001234567"
}
```

- `fondos`
```typescript
{
 {
  "_id": "101",
  "nombre": "FondoEjemplo",
  "montoMinimo": 100.0
}
```
- `transacciones`
```typescript
{
  "_id": "t1",
  "usuarioId": "1",
  "fondoId": "101",
  "monto": 500.0,
  "tipo": "APERTURA", // o "CANCELACION"
  "fecha": "2025-08-27T09:30:00"
}
```

## Endpoints Backend
Usuarios
- GET /api/usuarios/{id} - Obtener usuario por ID
- POST /api/usuarios/{id}/fondos/{fondoId}?monto={monto} - Suscribir usuario a un fondo
- DELETE /api/usuarios/{usuarioId}/fondos/{fondoId} - Cancelar suscripción
- GET /api/usuarios/{usuarioId}/transacciones - Obtener historial de transacciones

Transacciones
Las transacciones se manejan a través del endpoint de usuarios:
- GET /api/usuarios/{usuarioId}/transacciones - Obtener transacciones por usuario

