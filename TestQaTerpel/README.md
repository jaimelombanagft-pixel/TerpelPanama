# Terpel App (Android)

Aplicación Android nativa desarrollada en Kotlin siguiendo MVVM y Jetpack (Navigation, ViewBinding, Material Design 3). El proyecto implementa el flujo inicial de bienvenida, registro (multi-paso), verificación por OTP y pantalla Home con acciones rápidas e imágenes optimizadas mediante Coil.

## 1. Título y descripción general

- **Nombre del proyecto**: TerpelApp
- **Paquete**: `com.terpel.app`
- **Arquitectura**: MVVM + Jetpack Navigation + ViewBinding
- **UI**: XML layouts + Material Design 3
- **Imágenes**: Cargadas con Coil (caché, crossfade)

## 2. Requisitos previos

- **Sistema operativo**: macOS, Windows o Linux
- **Android Studio**: versión estable actual (recomendado 2024.x o superior)
- **JDK**: 17
- **Gradle Wrapper**: `9.0-milestone-1` (gestionado por wrapper)
- **Android Gradle Plugin (AGP)**: `8.6.1`
- **Kotlin**: `2.0.21` (plugin)
- **SDK**:
  - `compileSdk`: 34
  - `targetSdk`: 34
  - `minSdk`: 26

Principales dependencias del módulo `app`:

```kotlin
implementation("androidx.core:core-ktx:1.13.1")
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
implementation("androidx.navigation:navigation-ui-ktx:2.8.4")
implementation("androidx.viewpager2:viewpager2:1.0.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
implementation("io.coil-kt:coil:2.6.0")
```

Librerías de test:

```kotlin
testImplementation("junit:junit:4.13.2")
testImplementation("androidx.arch.core:core-testing:2.2.0")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
androidTestImplementation("androidx.test.ext:junit:1.2.1")
androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
androidTestImplementation("androidx.test:rules:1.6.1")
androidTestImplementation("androidx.test:runner:1.6.1")
androidTestImplementation("androidx.navigation:navigation-testing:2.8.4")
```

## 3. Instalación y configuración

1. Instalar Android Studio y configurar JDK 17.
2. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd TestQaTerpel
   ```
3. Abrir la carpeta del proyecto en Android Studio.
4. Esperar la sincronización de Gradle y descarga de dependencias.
5. Seleccionar una `Device` virtual (AVD) o conectar un dispositivo físico.
6. Ejecutar el proyecto:
   - Desde Android Studio: `Run` ▶
   - O por consola:
     ```bash
     ./gradlew assembleDebug
     ./gradlew installDebug
     ```

Comandos útiles:

```bash
# Lint y análisis estático
./gradlew lint

# Unit tests
./gradlew test

# Instrumented tests (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
```

## 4. Estructura del proyecto

```
TestQaTerpel/
├── settings.gradle.kts
├── gradle/wrapper/gradle-wrapper.properties
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/terpel/app/
        │   ├── MainActivity.kt
        │   └── ui/
        │       ├── welcome/         # Onboarding + Login/Registro
        │       ├── register/        # Registro Step1/Step2/Step3 + VM
        │       ├── otp/             # Verificación OTP
        │       └── home/            # Pantalla Home
        └── res/
            ├── layout/              # XML layouts
            ├── navigation/          # nav_graph.xml
            └── values/              # strings, themes, colors
```

- `MainActivity.kt`: Host del `NavHostFragment` definido en `activity_main.xml`.
- `res/navigation/nav_graph.xml`: Mapa de navegación entre Welcome → Login/Registro → OTP → Home.
- `ui/*`: Feature-first, cada pantalla con su `Fragment` y ViewModel cuando aplica.
- `values/themes.xml`: Tema `Material3` con colores de marca.

## 5. Guía de uso (flujos principales)

- **Bienvenida**: Dos botones visibles y funcionales.
  - `Iniciar sesión` → navega a Login.
  - `Registrarme` → navega a Registro.
- **Registro**:
  - Paso 1: Documento + Celular con validación y habilitación progresiva del botón.
  - Paso 2: Validación de teléfono, documento, contraseña/confirmación y checkbox de términos encima del botón `Continuar`.
  - Paso 3: Autorización y avance a OTP.
- **OTP**: Verifica código y luego navega a Home.
- **Home**: Muestra greeting, puntos y acciones rápidas; imágenes cargadas con Coil.

### Ejemplos prácticos

Cargar imágenes con Coil:

```kotlin
binding.headerImage.load("https://www.terpelpanama.com/images/slider-vayven-h-0.jpg") {
    crossfade(true)
    placeholder(R.drawable.ic_terpel_placeholder)
}
```

Navegación entre pantallas:

```kotlin
findNavController().navigate(R.id.action_otp_to_home)
```

Definición del `NavHost`:

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/nav_host"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/nav_graph"
    android:name="androidx.navigation.fragment.NavHostFragment"/>
```

## 6. Configuraciones especiales y variables de entorno

- **Permisos**: `INTERNET` habilitado en `AndroidManifest.xml` para carga remota de imágenes.
- **Variables de entorno**: No se requieren variables externas ni secretos en esta fase.
- **JDK**: Asegurar que el proyecto usa JDK 17 en `Gradle Settings`.

## 7. Cómo contribuir

- Usar Kotlin y seguir el estilo oficial (`kotlin.code.style=official`).
- Seguir arquitectura MVVM y patrones Jetpack.
- Escribir código limpio (SOLID, DRY, KISS, YAGNI).
- Antes de abrir PR:
  - Ejecutar `./gradlew lint test` y corregir errores.
  - Probar la navegación en dispositivo/emulador.
- Convenciones sugeridas:
  - Branches: `feature/...`, `fix/...`, `chore/...`.
  - Commits: mensajes descriptivos en imperativo.

## 8. Licencia

Proyecto de uso **interno**. Todos los derechos reservados © Terpel Panamá. La redistribución o uso fuera del equipo autorizado requiere aprobación. Actualizar esta sección si se adopta una licencia específica.

## 9. Contacto

- Equipo del proyecto (REDMI) — canales internos (Slack/Teams).
- Puntos de contacto: Product Owner, Líder Técnico, QA.
- Actualice esta sección con nombres y correos institucionales.

---

### Notas

- Dependencias y versiones se toman del `app/build.gradle.kts` y del wrapper del proyecto.
- El tema visual usa Material Design 3 con colores de marca en `values/themes.xml`.
- Imágenes remotas optimizadas mediante Coil con `crossfade` y `placeholder`.
