# Guía de Uso en Visual Studio Code

Este proyecto ha sido transformado para una experiencia óptima en VS Code.

## Cambios Realizados

1.  **Estructura de Carpetas:** Se han movido todos los archivos de la carpeta `demo/` a la raíz del proyecto para que sea un proyecto Maven estándar.
2.  **Versión de Java:** Se ha ajustado la versión de Java a la **17** en el `pom.xml` para asegurar compatibilidad con el entorno actual.
3.  **Dependencias:** Se han añadido las dependencias necesarias que faltaban:
    *   `spring-boot-starter-security`: Para el manejo de usuarios y roles.
    *   `spring-boot-starter-thymeleaf`: Para procesar las plantillas HTML.
    *   `thymeleaf-extras-springsecurity6`: Para usar etiquetas de seguridad en HTML.
4.  **Configuración de VS Code:** Se ha creado la carpeta `.vscode/` con:
    *   `launch.json`: Permite ejecutar y depurar la aplicación directamente desde la pestaña "Run and Debug".
    *   `extensions.json`: Recomienda las extensiones esenciales (Extension Pack for Java, Spring Boot Extension Pack).
    *   `settings.json`: Configura el encoding a UTF-8 y la detección automática de Maven.
5.  **Codificación:** Se ha corregido el archivo `application.properties` para usar codificación UTF-8.

## Pasos para ejecutar el proyecto

### 1. Preparar la Base de Datos
Asegúrate de tener MySQL instalado y ejecuta el script SQL incluido en la raíz:
```bash
mysql -u root -p < BDTFG.sql
```
*Si tu usuario o contraseña de MySQL son diferentes a `root` / `1234`, cámbialos en `src/main/resources/application.properties`.*

### 2. Abrir en VS Code
1.  Abre la carpeta raíz `tfg-backend` en Visual Studio Code.
2.  Si VS Code te pide instalar las extensiones recomendadas, acéptalo.
3.  Espera a que la extensión de Java importe el proyecto Maven (verás una barra de progreso en la esquina inferior derecha).

### 3. Ejecutar la Aplicación
Existen tres formas de ejecutarla:
*   **Desde "Run and Debug":** Presiona `F5` o ve a la pestaña de "Run and Debug" y elige "DemoApplication".
*   **Desde el Dashboard de Spring Boot:** Si tienes instalada la extensión de Spring Boot, verás un icono de una hoja en la barra lateral donde puedes iniciar la app.
*   **Desde la Terminal:**
    ```bash
    ./mvnw spring-boot:run
    ```

## Notas Adicionales
*   Se han eliminado las carpetas `.idea` (IntelliJ) para evitar conflictos.
*   Se han mantenido los archivos `.gitignore` y `.gitattributes` originales (renombrados temporalmente si fue necesario).
