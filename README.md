# ReporteService

API REST del examen para reportes de acumulación de basura. Java 21, Spring Boot 3 y Spring Data JPA; PostgreSQL en producción y H2 persistente para desarrollo local.

## Ejecutar localmente

Usa JDK 21; Android Studio incluye un JDK 21 que puedes seleccionar como Gradle JDK. Desde esta carpeta ejecuta:

```powershell
gradlew.bat bootRun
```

La API escucha en `http://localhost:8080`. Para PostgreSQL local, define `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` y `SPRING_DATASOURCE_DRIVER=org.postgresql.Driver`. Por defecto usa H2 en `./data/reportes`.

## Endpoints

- `POST /reportes/` — crear reporte (el servidor fija estado 1).
- `GET /reportes/?offset=0&limit=5` — página de reportes.
- `GET /reportes/{id}` — detalle.
- `GET /reportes/ciudadano/{dni}` — reportes del DNI.
- `PUT /reportes/{id}` — atender (estado 0).

Ejemplo de alta:

```json
{
  "dni": "12345678",
  "tipo": "DOMICILIARIA",
  "direccion": "Av. Los Álamos 123",
  "referencia": "Esquina del parque"
}
```

La fecha es opcional y se asigna en el servidor. Nunca publiques contraseñas ni archivos `.env`.

En la primera ejecución con H2 local, una migración de desarrollo compacta una sola vez los IDs existentes y reinicia el autoincremento; PostgreSQL en producción no se renumera.

## Render

Crear PostgreSQL y un Web Service desde este directorio usando el Dockerfile. Configurar `SPRING_DATASOURCE_URL` (JDBC internal URL), `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` y `SPRING_DATASOURCE_DRIVER=org.postgresql.Driver`. Render configura `PORT`. El parámetro `offset` cuenta registros y se convierte a página; usa saltos del tamaño de `limit` (0, 5, 10...).
