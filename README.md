# 🐾 PetShield - Aplicación Nativa Android

**PetShield** es una aplicación móvil nativa para Android diseñada para facilitar la atención veterinaria y el cuidado integral de mascotas. La plataforma permite a los propietarios de mascotas agendar citas veterinarias, gestionar el carnet digital de vacunación, guardar clínicas y profesionales favoritos, y realizar un seguimiento continuo de la salud de sus animales de compañía.

---

## 🚀 Tecnologías y Arquitectura

* **Lenguaje:** [Kotlin](https://kotlinlang.org/) (Target JVM 11)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) con **Material Design 3**
* **Arquitectura:** **MVVM** (*Model-View-ViewModel*) con Flujo Unidireccional de Datos (UDF)
* **Backend & Servicios:** [Google Firebase](https://firebase.google.com/)
  * **Firebase Authentication:** Registro e inicio de sesión de usuarios.
  * **Cloud Firestore:** Base de datos NoSQL reactiva para la sincronización en tiempo real de usuarios, mascotas, clínicas, vacunas y citas.
* **Asincronía:** Kotlin Coroutines & StateFlow / State (`kotlinx-coroutines-play-services`)
* **SDK Mínimo:** API 24 (Android 7.0 Nougat) | **SDK Target:** API 35 (Android 15)

---

## 🎨 Guía de Estilos y Paleta de Colores

PetShield utiliza un sistema de diseño limpio, moderno y amigable orientado a la tranquilidad del usuario y la salud animal.

### 🎨 Tabla de Colores (`Color.kt`)

| Token de Color | Código Hex / Valor | Previsualización | Uso Principal |
| :--- | :--- | :--- | :--- |
| `CelesteInicio` | `#33E4DB` | 🟦 `#33E4DB` | Gradientes iniciales, acentos secundarios, acentuación de botones |
| `CelesteFin` | `#00BBD3` | 🟦 `#00BBD3` | **Color Primario**, botones principales, app bars y branding |
| `PetShieldGradient` | `#33E4DB` ➔ `#00BBD3` | 🌊 Gradiente Horizontal | Fondos en pantallas de bienvenida, encabezados y Splash |
| `FondoClaro` | `#E9F6FE` | ⬜ `#E9F6FE` | Contenedores de tarjetas, fondos secundarios y destacados |
| `TextoOscuro` | `#252525` | ⬛ `#252525` | Color de texto principal en fondos claros e insumos de texto |
| `ErrorRedBg` | `#FFEBEE` | 🟥 `#FFEBEE` | Fondo para alertas, errores y confirmaciones de cancelación |
| `ErrorRedText` | `#D32F2F` | 🟥 `#D32F2F` | Texto destructivo (p. ej., *Cancelar Cita*) y mensajes de error |
| `White` | `#FFFFFF` | ⬜ `#FFFFFF` | Superficies de tarjetas, modales y fondos principales |

### ✏️ Estilos y Tipografía (`Type.kt` / Material 3)

| Elemento UI | Estilo / Configuración | Descripción |
| :--- | :--- | :--- |
| **Tema Base** | `MaterialTheme` (Material 3) | Adaptable a modo claro/oscuro mantención de contraste |
| **Tipografía Base** | `bodyLarge` (16sp, Line Height 24sp) | Lectura cómoda en formularios y detalles |
| **Formas (Shapes)** | Rounded Corner (8dp - 16dp) | Bordes redondeados en botones, imágenes y tarjetas |
| **Navegación Superior** | `PetShieldTopBar` | Barra modular con soporte para botón de retroceso y título dinámico |
| **Navegación Inferior** | `PetShieldBottomBar` | Barra flotante o anclada con accesos rápidos a Home, Citas y Perfil |

---

## 📱 Funcionalidades y Pantallas Principales

1. **Autenticación y Bienvenida**
   * **Splash & Welcome:** Introducción de marca con gradiente nativo.
   * **Login & Registro:** Autenticación mediante Firebase Auth con validación de credenciales.
   * **Recuperación de Contraseña:** Restablecimiento mediante correo electrónico.
   * **Onboarding:** Guía interactiva de bienvenida al primer ingreso.

2. **Gestión de Mascotas y Salud**
   * **Carnet de Vacunación:** Registro detallado de vacunas completadas y pendientes por mascota.
   * **Agregar Mascota:** Registro de datos clave (nombre, especie, raza, peso, fecha de nacimiento y sexo).

3. **Citas Veterinarias y Clínicas**
   * **Directorio de Clínicas:** Búsqueda y filtrado de centros veterinarios recomendados.
   * **Reserva de Servicios:** Selección de profesional, modalidad (presencial/domicilio), fecha y hora.
   * **Resumen de Cita:** Desglose del costo del servicio y tarifa.
   * **Mis Citas:** Historial y estado de citas (Próximas y Pasadas) con opción de cancelación fundamentada.

4. **Perfil y Soporte**
   * **Perfil de Usuario:** Datos de contacto y accesos directos.
   * **Favoritos:** Lista de clínicas y veterinarios preferidos.
   * **FAQ:** Centro de ayuda y preguntas frecuentes interactivo.

---

## 📂 Estructura del Proyecto

```
app/src/main/java/com/example/petshield/
├── MainActivity.kt            # Punto de entrada y gestión del backstack de navegación
├── PetShieldState.kt          # ViewModel principal (PetShieldViewModel) y estados de UI
├── PetShieldTopBar.kt         # Componente de la barra superior reutilizable
├── PetShieldBottomBar.kt      # Componente de la barra de navegación inferior
├── data/                      # Capa de datos y persistencia
│   ├── PetShieldModels.kt     # Modelos de dominio y DTOs para Cloud Firestore
│   └── FirebaseRepository.kt  # Cliente de integración con Firebase Auth y Firestore
├── ui/theme/                  # Sistema de diseño de la aplicación
│   ├── Color.kt               # Paleta de colores y definiciones de gradientes
│   ├── Theme.kt               # Definición del esquema Material 3 (PetShieldTheme)
│   └── Type.kt                # Configuración de tipografías
└── [Pantallas]                # Componentes Composable de cada pantalla
    ├── HomeScreen.kt
    ├── LoginScreen.kt
    ├── ClinicasScreen.kt
    ├── CarnetVacunasScreen.kt
    ├── DetalleServicioScreen.kt
    └── ...
```

---

## 🛠️ Requisitos e Instalación

### Prerrequisitos
* **Android Studio** (Koala / Ladybug / 2024.1+ recomendado)
* **JDK 11** configurado en el proyecto
* Dispositivo físico o emulador con **Android 7.0 (API 24)** o superior

### Pasos para Ejecutar
1. **Clonar el repositorio:**
   ```bash
   git clone <url-del-repositorio>
   cd Entrega3AplicacionNativa-main
   ```
2. **Configuración de Firebase:**
   * Asegúrate de contar con el archivo `google-services.json` ubicado dentro del directorio `/app`.
3. **Compilar y Ejecutar:**
   * Abre el proyecto en Android Studio.
   * Sincroniza Gradle (`Gradle Sync`).
   * Ejecuta el comando o presiona **Run** en Android Studio:
     ```bash
     ./gradlew assembleDebug
     ```

---

*Desarrollado para la entrega de la Aplicación Nativa Android - PetShield.* 🐶🐱✨
