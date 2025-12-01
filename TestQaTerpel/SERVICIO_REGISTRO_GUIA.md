# Guía de Configuración del Servicio de Registro

## 📋 Resumen Técnico

Se ha implementado un servicio completo de registro de usuarios utilizando:
- **Retrofit 2.9.0** para llamadas HTTP
- **Gson** para serialización/deserialización JSON
- **Coroutines** para operaciones asincrónicas
- **Repository Pattern** para abstracción de datos
- **ViewModel** para gestión de estado

## 🔧 Cambiar la URL del Servidor

Si necesitas cambiar la URL del servidor, edita el archivo:

```
/app/src/main/java/com/terpel/app/api/RetrofitClient.kt
```

Busca la línea:
```kotlin
private const val BASE_URL = "http://localhost:3000/services/apexrest/"
```

Y reemplázala con tu URL:
```kotlin
private const val BASE_URL = "https://tu-servidor.com/services/apexrest/"
```

## 📊 Estructura de Clases

```
RegisterStep3Fragment (UI)
        ↓
RegisterViewModel (Estado y Lógica)
        ↓
RegisterRepository (Abstracción de datos)
        ↓
RegisterRemoteDataSource (Acceso a datos remoto)
        ↓
RetrofitClient → TerpelApiService (HTTP)
        ↓
Servidor (Backend)
```

## 📤 Datos Enviados al Servidor

Los datos que se envían al registrar un usuario:

```json
{
  "FirstName": "Juan",              // Del paso 2
  "FirstLastName": "Pérez",         // Del paso 2
  "DocumentTypeName": "CC",         // Del paso 1
  "Document": "1234567890",         // Del paso 1
  "Country": "Panamá",              // Hardcoded
  "CellPhone": "3001234567",        // Del paso 1
  "Email": "juan@example.com",      // Del paso 2
  "VehicleType": "Particular",      // Del paso 2
  "AgreedTerms": true,              // Checkbox paso 3
  "AgreedHD": true,                 // Hardcoded
  "HDDate": "28/11/2025 10:30 AM",  // Fecha actual
  "HDVersion": "1.0",               // Hardcoded
  "AgreedTC": true,                 // Hardcoded
  "TCDate": "28/11/2025 10:30 AM",  // Fecha actual
  "TCVersion": "1.0",               // Hardcoded
  "IP": "0.0.0.0",                  // Hardcoded
  "Program": "LoyaltyProgram"       // Hardcoded
}
```

## 📥 Respuesta Esperada del Servidor

```json
{
  "statusCode": 200,
  "message": "Usuario registrado exitosamente",
  "success": true,
  "data": {
    "userId": "USER123",
    "email": "juan@example.com",
    "document": "1234567890",
    "firstName": "Juan",
    "lastName": "Pérez",
    "status": "ACTIVE"
  }
}
```

## 🐛 Debugging

### Ver logs del servidor

En logcat, filtra por el tag `RegisterStep3`:

```bash
adb logcat RegisterStep3:* *:S
```

### Errores Comunes

1. **"http.ClientProtocolException"** - El servidor no está corriendo
2. **"UnknownHostException"** - El dominio no es accesible
3. **"HTTP 404"** - El endpoint no existe en el servidor
4. **"JSON parsing error"** - La respuesta del servidor no coincide con el modelo

## 🔄 Flujo de Registro

1. Usuario completa pasos 1-3
2. Usuario hace clic en "Siguiente" en paso 3
3. `RegisterStep3Fragment` llama a `viewModel.registerUser()`
4. `RegisterViewModel` crea el request y llama al repositorio
5. `RegisterRepository` transforma datos y llama al datasource
6. `RegisterRemoteDataSource` usa Retrofit para HTTP POST
7. `TerpelApiService` envía solicitud al servidor
8. Se recibe respuesta del servidor
9. Si éxito: navega a OTP
10. Si error: muestra Snackbar con mensaje de error

## 🔐 Seguridad

- Todas las llamadas de red ocurren en background threads (Coroutines)
- Validación de datos antes de enviar
- Manejo de errores seguro
- HTTPS listo para producción (cambiar URL)

## 📝 Agregar Campos Nuevos

Si necesitas agregar campos nuevos:

1. Agrega el campo a `RegisterRequest.kt`:
```kotlin
@SerializedName("NuevoCampo")
val nuevoData: String = ""
```

2. Agrega el campo a `RegisterFormState`:
```kotlin
val nuevoData: String = ""
```

3. Agrega un getter en `RegisterViewModel`:
```kotlin
fun onNuevoDataChanged(value: String) {
    _state.value = _state.value!!.copy(nuevoData = value)
}
```

4. Usa el nuevo campo en `registerUser()`:
```kotlin
nuevoData = s.nuevoData
```

## 📞 Contacto y Soporte

Para cambios en la API o problemas con el servidor, contacta al equipo de backend.
