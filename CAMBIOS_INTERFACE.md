# Cambios de Interfaz Gráfica - Terpel Panamá

## Resumen de Cambios

Se han realizado ajustes significativos a la interfaz gráfica para alinearla con las imágenes de referencia proporcionadas. Los cambios principales se enfocaron en el flujo de registro y la pantalla OTP.

## Archivos Modificados

### 1. **fragment_otp.xml** (Completamente Rediseñado)

#### Cambios Principales:

- ✅ **Header Image**: Agregada imagen de encabezado (35% de altura) con carga desde URL
- ✅ **Card Layout**: MaterialCardView con esquinas redondeadas (24dp) y padding
- ✅ **Step Indicator**: Indicador visual de 4 pasos con círculos conectados por líneas punteadas
  - Pasos 1-3: Marcados con ✓ (completados)
  - Paso 4: Número 4 (OTP - actual)
- ✅ **Título**: "¿Ya tienes tu código?" en color rojo Terpel
- ✅ **Subtítulo**: Texto descriptivo sobre el código de verificación
- ✅ **OTP Input**: Campo de entrada para 6 dígitos
- ✅ **Timer**: Muestra tiempo restante para reenvío
- ✅ **Botón Reenviar**: Habilitado después del timeout
- ✅ **Botón Verificar**: Button primario con color rojo Terpel
- ✅ **Progress Indicator**: Indicador circular que se muestra durante la verificación
- ✅ **NestedScrollView**: Envuelve el contenido para mejor comportamiento en pantallas pequeñas

### 2. **OtpFragment.kt** (Actualizado)

#### Cambios Principales:

- ✅ **Header Image Loading**: Carga de imagen desde URL usando biblioteca Coil
  - URL: `https://www.terpelpanama.com/images/linea-lubricantes-h.png`
  - Fallback: Imagen placeholder local
- ✅ **Progress Indicator**: Manejo de visibilidad durante verificación
- ✅ **Estado del Botón**: El botón de verificar se oculta cuando se muestra el progress
- ✅ **Transiciones Mejoradas**: Mantiene animaciones MaterialSharedAxis

#### Código Ejemplo:

```kotlin
binding.headerImage.load("https://www.terpelpanama.com/images/linea-lubricantes-h.png") {
    crossfade(true)
    placeholder(R.drawable.ic_terpel_placeholder)
}
```

## Flujo de Navegación

```
RegisterFragment (Paso 1: Documento, Celular)
    ↓ (Usuario completa datos + Siguiente)
RegisterStep2Fragment (Paso 2: Contraseña, Términos)
    ↓ (Usuario completa datos + Siguiente)
RegisterStep3Fragment (Paso 3: Autorización de datos)
    ↓ (Usuario acepta autorización + Siguiente)
OtpFragment (Paso 4: Código OTP) ← NUEVA INTERFAZ APLICADA
    ↓ (Usuario ingresa OTP válido)
HomeFragment
```

## Patrones de Diseño Consistentes

Todas las pantallas de registro ahora siguen un patrón visual consistente:

1. **Header Image** (35% de altura)
2. **MaterialCardView** con contenido
3. **Step Indicator** mostrando progreso
4. **Título** en rojo Terpel
5. **Campos de entrada** con validación
6. **Botón de acción** primario
7. **Progress indicator** durante operaciones

## Características Técnicas

### Colores Utilizados

- **Rojo Terpel**: `@color/terpel_red`
- **Azul Oscuro**: `@color/terpel_dark_blue`
- **Gris Claro**: `@color/terpel_light_gray`
- **Blanco**: `@color/white`

### Componentes Material

- `MaterialCardView`: Para contenedor principal
- `MaterialAutoCompleteTextView`: Para dropdowns
- `TextInputLayout`: Para campos con validación
- `MaterialButton`: Para botones de acción
- `CircularProgressIndicator`: Para indicador de carga
- `LinearProgressIndicator`: Para indicador de fortaleza

### Dimensiones Clave

- **Header Height**: 35% de la pantalla
- **Card Corner Radius**: 24dp
- **Padding General**: 24dp
- **Margin Between Elements**: 12-24dp
- **Circle Diameter**: 24dp

## Testing

Para verificar los cambios:

1. Ejecuta el flujo de registro desde el inicio
2. Completa el paso 1 (documento, celular)
3. Completa el paso 2 (contraseña, términos)
4. Completa el paso 3 (autorización de datos)
5. Haz clic en "Siguiente" en paso 3
6. **Resultado Esperado**: Deberías ver la nueva pantalla OTP con:
   - Header image de Terpel
   - Step indicator mostrando 4 pasos con ✓✓✓4
   - Campo para ingresar código OTP
   - Timer y botón de reenvío

## Notas Importantes

- La imagen de header se carga desde una URL remota. Asegúrate de que el dispositivo tenga conexión a internet durante el testing.
- El botón "Verificar" solo se habilita cuando se ingresan 6 dígitos.
- El botón "Reenviar" se habilita después del timeout del timer.
- El progress indicator se muestra automáticamente durante la verificación.

---

**Fecha de Actualización**: 30 de Noviembre, 2024  
**Versión**: 1.0
