# Guía de Conexión a Localhost desde Android Emulador

## Problema Resuelto

El error **"Failed to connect to localhost/127.0.0.1:3000"** ocurría porque en Android, `localhost` se refiere al emulador mismo, no al host (tu máquina).

## Solución Implementada

### 1. **URL Corregida en RetrofitClient.kt**
```kotlin
// ANTES (no funcionaba desde emulador):
private const val BASE_URL = "http://localhost:3000/services/apexrest/"

// AHORA (funciona desde emulador):
private const val BASE_URL = "http://10.0.2.2:3000/services/apexrest/"
```

**Por qué `10.0.2.2`?**
- En Android Emulator, `10.0.2.2` es un alias especial que se refiere al host (tu máquina)
- Esto es equivalente a `localhost` desde la perspectiva del emulador
- En dispositivos físicos, cambiar a la IP local del servidor (ej: `http://192.168.x.x:3000`)

### 2. **Logging Detallado Agregado**

Se implementó logging en tres niveles para diagnosticar problemas de conexión:

#### RetrofitClient.kt
```
Inicializando Retrofit con URL: http://10.0.2.2:3000/services/apexrest/
Obteniendo servicio de API
```

#### RegisterRemoteDataSource.kt
```
Iniciando llamada a registerUser con datos: {...}
Respuesta recibida - Código: 200, Exitosa: true
Registro exitoso: Usuario registrado correctamente

// O en caso de error:
Error de conexión: No se puede conectar al servidor. ¿El servidor está corriendo en 10.0.2.2:3000?
Error de red: Connection refused - Verifica tu conexión de internet
```

#### RegisterViewModel.kt
```
Iniciando registro con datos: fullName=Juan, email=juan@example.com, vehicleType=Particular
Registro exitoso: Usuario registrado correctamente

// O:
Error en registro: Error de conexión: No se puede conectar...
```

### 3. **Manejo de Errores Específicos**

Se capturan y registran diferentes tipos de errores:

| Error | Causa | Solución |
|-------|-------|----------|
| `ConnectException` | Servidor no está corriendo | Iniciar el servidor en `localhost:3000` |
| `SocketTimeoutException` | Servidor responde muy lento | Verificar que el servidor no esté sobrecargado |
| `IOException` | Problemas de red | Verificar conexión de internet |
| HTTP 4xx/5xx | Errores de la API | Verificar los datos enviados o el estado del servidor |

## Cómo Probar la Conexión

### 1. **Asegúrate que el servidor esté corriendo**
```bash
# En tu máquina local
curl -X POST http://localhost:3000/services/apexrest/ClientManagement/v2/ \
  -H 'Content-Type: application/json' \
  -d '{
    "FirstName":"Test",
    "FirstLastName":"User",
    "DocumentTypeName":"CC",
    "Document":"1234567890",
    "Country":"Panamá",
    "CellPhone":"3001234567",
    "Email":"test@example.com",
    "VehicleType":"Particular",
    "AgreedTerms":true,
    "AgreedHD":true,
    "HDDate":"30/11/2025 10:30 AM",
    "HDVersion":"1.0",
    "AgreedTC":true,
    "TCDate":"30/11/2025 10:30 AM",
    "TCVersion":"1.0",
    "IP":"192.168.1.100",
    "Program":"LoyaltyProgram"
  }'
```

Debe responder con `HTTP 200` y un objeto JSON con los datos del usuario registrado.

### 2. **Probar desde la app**
1. Abre la app en el emulador
2. Completa todos los campos:
   - Paso 1: Documento y Celular
   - Paso 2: Nombre, Apellido, Email, Tipo de Vehículo, Términos
   - Paso 3: Acepta autorización y presiona "Siguiente"
3. Observa el LogCat:
   ```bash
   adb logcat | grep -E "RegisterViewModel|RegisterRemoteDataSource|RetrofitClient"
   ```

### 3. **Ver los logs**
```bash
# Todos los logs de registro
adb logcat | grep "Register"

# Solo errores
adb logcat | grep "E/.*Remote"

# Logs específicos
adb logcat -s RegisterViewModel RegisterRemoteDataSource RetrofitClient
```

## Para Dispositivo Físico

Si quieres probar en un dispositivo Android físico en lugar del emulador:

1. Obtén la IP local de tu máquina:
   ```bash
   # En macOS/Linux
   ifconfig | grep "inet "
   # Busca algo como: 192.168.x.x o 10.0.x.x
   ```

2. Actualiza `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://192.168.x.x:3000/services/apexrest/"
   ```

3. Asegúrate que tu dispositivo esté en la misma red WiFi que tu máquina

4. Recompila e instala la app

## Comandos Útiles

```bash
# Ver todos los logs en tiempo real
adb logcat

# Ver solo logs de la app
adb logcat | grep "com.terpel.app"

# Ver logs filtrados por tag
adb logcat -s RegisterViewModel

# Limpiar logs
adb logcat -c

# Guardar logs a archivo
adb logcat > logs.txt &

# Ver solo errores y warnings
adb logcat | grep -E "^[EW]"
```

## Checklist de Solución de Problemas

- ✅ RetrofitClient.kt usa `10.0.2.2:3000` para emulador
- ✅ El servidor está corriendo en `localhost:3000`
- ✅ Los logs muestran "Inicializando Retrofit con URL: http://10.0.2.2:3000/services/apexrest/"
- ✅ La red del emulador está habilitada
- ✅ El firewall permite conexiones a puerto 3000
- ✅ Los datos son válidos (todos los campos requeridos)
- ✅ El servidor responde correctamente (prueba con curl primero)

## Referencias

- [Android Emulator Network Documentation](https://developer.android.com/studio/run/emulator-networking)
- [Retrofit Error Handling](https://square.github.io/retrofit/)
- [Kotlin Coroutines Networking](https://kotlin.github.io/kotlinx.coroutines/)
