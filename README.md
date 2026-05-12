# Sanos-Salvos-Notificaciones

Servicio de notificaciones en Spring Boot con integración Kafka y JWT.

## Puerto

- `8086`

## Endpoints

- `POST /api/notificaciones/send?message={message}&recipient={recipient}`

## Configuración principal

- `server.port=8086`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/notificaciones_db`
- `spring.datasource.username=postgres`
- `spring.datasource.password=password`
- `spring.kafka.bootstrap-servers=localhost:9092`
- `spring.kafka.consumer.group-id=notificaciones-group`
- `jwt.secret=mySecretKey`
- `jwt.expiration=86400000`

## Requisitos

- Java 17
- Maven
- PostgreSQL
- Kafka en `localhost:9092`

## Ejecución

```bash
cd Sanos-Salvos-Notificaciones
mvn clean package
mvn spring-boot:run
```

O con Docker: `docker-compose up notificaciones-service` desde la raíz del repositorio.