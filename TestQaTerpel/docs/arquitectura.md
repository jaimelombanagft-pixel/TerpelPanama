# Terpel App

## Arquitectura

- MVVM con `ViewModel` y `LiveData`.
- UI con `Activity` + `Fragments` y Views nativas.
- Navegación con `NavHostFragment` y `nav_graph`.
- Tematización con Material Design 3.

## Estructura

- `app/src/main/java/com/terpel/app/MainActivity.kt`
- `app/src/main/java/com/terpel/app/ui/welcome/WelcomeFragment.kt`
- `app/src/main/java/com/terpel/app/ui/welcome/WelcomeViewModel.kt`
- `app/src/main/res/layout/*`
- `app/src/main/res/navigation/nav_graph.xml`

## Pruebas

- Unit: `WelcomeViewModelTest` (JUnit + InstantTaskExecutorRule)
- UI: `WelcomeFragmentTest` (Espresso)

## Build APK firmado

- `debug` se firma automáticamente con keystore de debug.
- Para `release` configure variables de entorno: `TERPEL_KEYSTORE_PATH`, `TERPEL_KEYSTORE_PASSWORD`, `TERPEL_KEY_ALIAS`, `TERPEL_KEY_PASSWORD` y añada un `signingConfig` en `app/build.gradle.kts`.

## Rendimiento

- Verificar con Android Profiler al ejecutar en dispositivo/emulador.
