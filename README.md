# Práctica en Clase: ListView + Glide + Supabase SDK

Aplicación Android nativa desarrollada en Kotlin que se conecta de forma asíncrona a una base de datos en Supabase a través de su SDK oficial. Muestra un listado de estudiantes mediante un `ListView` con un adaptador personalizado (`AlumnoAdapter`), cargando imágenes dinámicamente con la librería **Glide** y aplicando transformaciones circulares.

---

## 📸 Capturas de Pantalla

> [!NOTE]
> Coloque aquí las capturas de pantalla de la aplicación en ejecución.
> E.g., `![MainActivity](screenshots/main_activity.png)`

---

## 🛠️ Tecnologías y Librerías Utilizadas

*   **Lenguaje**: Kotlin (v2.2.10 / stdlib v2.2.10)
*   **Diseño**: XML con `ConstraintLayout`, `LinearLayout`, `ImageView`, `Spinner`, `TextView` y `ListView`.
*   **Acceso a Datos**: SDK Oficial de Supabase (`postgrest-kt` + Ktor Client Engine para Android).
*   **Manejo de Imágenes**: Glide (con CircleCrop para la fotografía del alumno).
*   **Seguridad**: Inyección de variables sensibles mediante Gradle `BuildConfig` cargadas desde `local.properties`.

---

## 🔑 Configuración de Credenciales de Seguridad

De acuerdo con las directrices de seguridad, las credenciales de la base de datos de Supabase **no** se encuentran escritas en el código de la aplicación. Deben configurarse en el archivo local de propiedades.

### Pasos para configurar:

1.  Abre el archivo `local.properties` en la raíz de tu proyecto.
2.  Agrega las siguientes líneas al final del archivo reemplazando los valores correspondientes:
    ```properties
    SUPABASE_URL=https://hmudviqmtmmiahhfomcm.supabase.co
    SUPABASE_KEY=tu_clave_anon_publica_aqui
    ```
3.  Gradle leerá automáticamente estos valores durante el proceso de sincronización y generará las constantes accesibles en el código mediante `BuildConfig.SUPABASE_URL` y `BuildConfig.SUPABASE_KEY`.

---

## 🏗️ Clases Principales y Estructura

*   **[Alumno.kt](app/src/main/java/com/example/pec/Alumno.kt)**:
    Data class de Kotlin con anotación `@Serializable` para mapear los registros de alumnos de Supabase.
*   **[Materia.kt](app/src/main/java/com/example/pec/Materia.kt)**:
    Data class para almacenar y deserializar las materias del semestre consultadas.
*   **[AlumnoAdapter.kt](app/src/main/java/com/example/pec/AlumnoAdapter.kt)**:
    Adaptador personalizado que hereda de `ArrayAdapter<Alumno>`. Implementa el patrón *ViewHolder* para optimizar el rendimiento al reciclar las vistas de la lista, y carga fotos circulares con Glide.
*   **[MainActivity.kt](app/src/main/java/com/example/pec/MainActivity.kt)**:
    Punto de entrada de la aplicación. Configura la inicialización de Supabase, los Spinners de semestre y materia, y realiza consultas asíncronas dentro del contexto de corrutinas (`lifecycleScope.launch`).

---

## 🚀 Instrucciones de Instalación y Ejecución

### Requisitos previos:
*   Android Studio Ladybug (o posterior) compatible con Gradle 9.5
*   JDK 17 o superior (como el JetBrains Runtime provisto en Android Studio)

### Pasos:
1.  Clonar este repositorio en su entorno local:
    ```bash
    git clone <url_de_tu_repositorio>
    ```
2.  Importar el proyecto en Android Studio.
3.  Crear y configurar las claves de Supabase en `local.properties` (ver sección [Configuración de Credenciales](#-configuración-de-credenciales-de-seguridad)).
4.  Compilar el proyecto e instalar en un emulador o dispositivo físico mediante el botón **Run** de Android Studio.

---

## 🔒 Restricciones de Desarrollo Cumplidas

*   Se utilizó estrictamente un **ListView** y un **ArrayAdapter** personalizado en lugar de RecyclerView o Jetpack Compose.
*   El renderizado y cargado de imágenes se gestiona puramente a través de **Glide** aplicando **CircleCrop**.
*   Toda la persistencia y carga de datos se realiza de manera asíncrona mediante el **Supabase SDK**.
