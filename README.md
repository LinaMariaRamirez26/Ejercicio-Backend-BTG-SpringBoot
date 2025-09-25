# Configuración del Backend Fondos App

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
  "_id": "usuario-unico",
  "nombre": "Usuario Unico",
  "saldo": 500000,
  "fondosSuscritos": [],
  "email": "pruebatecnica@gmail.com",
  "telefono": "+57311234567",
  "preferencia": "Email"
}

```

- `fondos`
```typescript
{ "_id": "1", "nombre": "FPV_BTG_PACTUAL_RECAUDADORA", "montoMinimo": 75000, "categoria": "FPV" }
{ "_id": "2", "nombre": "FPV_BTG_PACTUAL_ECOPETROL", "montoMinimo": 125000, "categoria": "FPV" }
{ "_id": "3", "nombre": "DEUDAPRIVADA", "montoMinimo": 50000, "categoria": "FIC" }
{ "_id": "4", "nombre": "FDO-ACCIONES", "montoMinimo": 250000, "categoria": "FIC" }
{ "_id": "5", "nombre": "FPV_BTG_PACTUAL_DINAMICA", "montoMinimo": 100000, "categoria": "FPV" }
```
- `transacciones`
```typescript
{
   "_id":"4d7af525-0094-4eb7-bc56-902bfd1d5ba6",
   "usuarioId":"usuario-unico",
   "fondoId":"5",
   "monto":120000,
   "tipo":"APERTURA",
   "fecha":2025-08-27T03:15:57.478+00:00,
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

