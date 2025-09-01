# dy-commons

`dy-commons` — это набор Spring Boot стартеров, которые предоставляют общие функциональные возможности для сервисов
Digital Yard.

## Доступные стартеры

### 1. [rest-commons-starter](rest-commons-starter/README.md) (v1.0.2)

Стартер для Spring Boot, предоставляющий единую систему обработки исключений и форматирования ошибок для REST API.

**Возможности:**

- Единый формат ответов для всех ошибок API
- Автоматическая обработка исключений Spring (валидация, методы HTTP и т.д.)
- Поддержка кастомных исключений с детализированной информацией
- Автоконфигурация `ObjectMapper` с поддержкой Java Time API

---

### 2. [web-security-starter](web-security-starter/README.md) (v1.0.2)

Стартер для Spring Boot, добавляющий поддержку JWT-аутентификации и базовых правил безопасности.

**Возможности:**

- Валидация JWT по публичному X.509 сертификату
- Извлечение пользователя и ролей из claims
- Фильтр `Authorization: Bearer ...`
- Единый формат ошибок через `rest-commons-starter`
- Настройка CORS и открытых эндпоинтов через `application.yaml`

---

### 3. [tracing-starter](tracing-starter/README.md) (v1.0.2)

Стартер для Spring Boot, обеспечивающий централизованный механизм отправки метрик,
логов и трейсов в коллекторы, поддерживающие OTLP.

**Возможности:**

- **Трейсинг**: Автоматическая настройка Micrometer Tracing с мостом к OpenTelemetry. Экспорт трейсов в OTLP-эндпоинт (
  gRPC/HTTP).
- **Метрики**: Экспорт метрик через Micrometer OTLP registry (HTTP).
- **Логи**: Интеграция с Logback для отправки логов в OTEL через OpenTelemetry Log Appender (gRPC/HTTP).

## Как подключить

Каждый стартер подключается через Gradle/Maven по отдельности:

```groovy
implementation "ru.digitalyard.commons:rest-commons-starter:1.0.2"
implementation "ru.digitalyard.commons:web-security-starter:1.0.2"
implementation "ru.digitalyard.commons:tracing-starter:1.0.2"
```

## Быстрый справочник

### 1. Коды ошибок REST API (из `rest-commons-starter`)

| Код ошибки                   | HTTP статус | Пример причины                               |
|------------------------------|-------------|----------------------------------------------|
| NOT\_FOUND                   | 404         | Некорректный URL                             |
| REQUIRED\_PARAMETER\_MISSING | 400         | Отсутствует обязательный параметр запроса    |
| ARGUMENT\_TYPE\_MISMATCH     | 400         | Неверный тип параметра                       |
| VALIDATION\_FAILED           | 400         | Ошибка валидации @RequestBody или параметров |
| METHOD\_NOT\_ALLOWED         | 405         | HTTP-метод не поддерживается                 |
| HTTP\_MESSAGE\_NOT\_READABLE | 400         | Некорректный JSON или пустое тело запроса    |
| UNSUPPORTED\_MEDIA\_TYPE     | 415         | Неподдерживаемый Content-Type                |
| ENTITY\_NOT\_FOUND           | 404         | Сущность не найдена в базе данных            |
| SERVICE\_UNAVAILABLE         | 500         | Сервис недоступен                            |
| UNKNOWN\_INTERNAL\_ERROR     | 500         | Неизвестная ошибка                           |

### 2. Ошибки безопасности (из `web-security-starter`)

| Код ошибки     | HTTP статус | Пример причины                          |
|----------------|-------------|-----------------------------------------|
| UNAUTHORIZED   | 401         | Токен отсутствует                       |
| TOKEN\_EXPIRED | 401         | Токен просрочен                         |
| INVALID\_TOKEN | 401         | Неверная подпись токена                 |
| FORBIDDEN      | 403         | Недостаточно прав для доступа к ресурсу |

---

### JWT claims (из `web-security-starter`)

| Claim        | Описание                  |
|--------------|---------------------------|
| sub          | UUID пользователя         |
| LOGIN        | Логин пользователя        |
| FIRST\_NAME  | Имя пользователя          |
| MIDDLE\_NAME | Отчество пользователя     |
| LAST\_NAME   | Фамилия пользователя      |
| USER\_RIGHTS | Список ролей пользователя |

---

### Переменные среды
#### web-security-starter

| Переменная                                    | По умолчанию                                  | Соответствующая property                 | Описание                                                                                   |
|-----------------------------------------------|-----------------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------------|
| `DY_AUTH_PUBLIC_KEY_PATH`                     | `classpath:public.key`                        | `dy.auth.public-key-path`                | Путь к файлу публичного ключа (X.509). Поддерживает форматы `classpath:`, `file:`, `http:` |
| `DY_AUTH_WEB_SECURITY_PERMISSIONS`            | `[]`                                          | `dy.web-security.permissions`            | Массив URL-шаблонов, доступных без аутентификации                                          |
| `DY_AUTH_WEB_SECURITY_CORS_ALLOWED_ORIGINS`   | `["*"]`                                       | `dy.web-security.cors.allowed-origins`   | Разрешенные домены для CORS                                                                |
| `DY_AUTH_WEB_SECURITY_CORS_ALLOWED_METHODS`   | `["GET", "POST", "PUT", "DELETE", "OPTIONS"]` | `dy.web-security.cors.allowed-methods`   | Разрешенные HTTP-методы                                                                    |
| `DY_AUTH_WEB_SECURITY_CORS_ALLOWED_HEADERS`   | `["*"]`                                       | `dy.web-security.cors.allowed-headers`   | Разрешенные заголовки запроса                                                              |
| `DY_AUTH_WEB_SECURITY_CORS_ALLOW_CREDENTIALS` | `false`                                       | `dy.web-security.cors.allow-credentials` | Разрешение передачи cookies                                                                |
| `DY_AUTH_WEB_SECURITY_CORS_MAX_AGE`           | `1h`                                          | `dy.web-security.cors.max-age`           | Время кэширования CORS-префлайт запросов                                                   |

#### tracing-starter

| Переменная                          | По умолчанию                                             | Соответствующая property                          | Описание                                                                      |
|-------------------------------------|----------------------------------------------------------|---------------------------------------------------|-------------------------------------------------------------------------------|
| `OTLP_LOGGING_ENDPOINT`             | `http://localhost:4317`                                  | `management.otlp.logging.endpoint`                | OTLP эндпоинт для логов                                                       |
| `OTLP_LOGGING_TRANSPORT`            | `grpc`                                                   | `management.otlp.logging.transport`               | Протокол для логов (grpc/http)                                                |
| `OTLP_LOGGING_TIMEOUT`              | `10s`                                                    | `management.otlp.logging.timeout`                 | Максимальное время ожидания для экспорта логов.                               |
| `OTLP_LOGGING_CONNECT_TIMEOUT`      | `10s`                                                    | `management.otlp.logging.connectTimeout`          | Время ожидания установления соединения для экспорта логов                     |
| `OTLP_LOGGING_COMPRESSION`          | `none`                                                   | `management.otlp.logging.compression`             | Метод сжатия для логов (например, none, gzip)                                 |
| `TRACING_SAMPLING_PROBABILITY`      | `1`                                                      | `management.tracing.sampling.probability`         | Вероятность семплинга трассировок (0.0-1.0)                                   |
| `OTLP_TRACING_ENDPOINT`             | `http://localhost:4317`                                  | `management.otlp.tracing.endpoint`                | OTLP эндпоинт для трассировок                                                 |
| `OTLP_TRACING_TRANSPORT`            | `grpc`                                                   | `management.otlp.tracing.transport`               | Протокол для трассировок (grpc/http)                                          |
| `OTLP_TRACING_TIMEOUT`              | `10s`                                                    | `management.otlp.tracing.timeout`                 | Максимальное время ожидания для экспорта трассировок                          |
| `OTLP_TRACING_COMPRESSION`          | `none`                                                   | `management.otlp.tracing.compression`             | Метод сжатия для трассировок (например, none, gzip)                           |
| `OTLP_METRICS_ENDPOINT`             | `http://localhost:4318/v1/metrics`                       | `management.otlp.metrics.export.url`              | OTLP эндпоинт для метрик                                                      |
| `OTLP_METRICS_BASE_TIME_UNIT`       | `SECONDS`                                                | `management.otlp.metrics.export.baseTimeUnit`     | Базовая единица времени для экспорта метрик (например, SECONDS, MILLISECONDS) |
| `OTLP_METRICS_ENABLED`              | `true`                                                   | `management.otlp.metrics.export.enabled`          | Включить экспорт метрик по OTLP                                               |
| `OTLP_METRICS_STEP`                 | `30s`                                                    | `management.otlp.metrics.export.step`             | Интервал экспорта метрик                                                      |
| `PROMETHEUS_METRICS_EXPORT_ENABLED` | `false`                                                  | `management.prometheus.metrics.export.enabled`    | Включить экспорт метрик для Prometheus                                        |
| `ENDPOINTS_WEB_EXPOSURE_INCLUDE`    | `health,info,prometheus`                                 | `management.endpoints.web.exposure.include`       | Список эндпоинтов, доступных через веб-интерфейс                              |
| `APPENDER_CONSOLE_ENABLE`           | `true`                                                   | `logging.appender.console.enable`                 | Включить консольный вывод логов                                               |
| `APPENDER_FILE_ENABLE`              | `false`                                                  | `logging.appender.file.enable`                    | Включить файловый вывод логов                                                 |
| `APPENDER_OTLP_ENABLE`              | `true`                                                   | `logging.appender.otlp.enable`                    | Включить OTLP аппендер для логов                                              |
| `LOG_FILE_NAME`                     | `${spring.application.name}.log`                         | `logging.file.name`                               | Имя файла лога                                                                |
| `LOG_FILE_PATH`                     | `/var/app-logs`                                          | `logging.file.path`                               | Путь к файлам логов                                                           |
| `LOG_FILE_PATTERN`                  | `${LOG_FILE_PATH}/archived/app.%d{yyyy-MM-dd}.%i.log.gz` | `logging.logback.rollingpolicy.file-name-pattern` | Шаблон имени для архивированных файлов логов                                  |
| `LOG_FILE_ROLLING_MAX_FILE_SIZE`    | `50MB`                                                   | `logging.logback.rollingpolicy.max-file-size`     | Максимальный размер файла лога до его ротации                                 |
| `LOG_FILE_ROLLING_MAX_HISTORY`      | `14`                                                     | `logging.logback.rollingpolicy.max-history`       | Максимальное количество дней хранения архивированных логов                    |
| `LOG_FILE_ROLLING_TOTAL_SIZE_CAP`   | `5GB`                                                    | `logging.logback.rollingpolicy.total-size-cap`    | Общий лимит размера всех архивированных логов                                 |

## Структура проекта

```
dy-commons/
├── rest-commons-starter/
│   └── README.md
├── web-security-starter/
│   └── README.md
├── tracing-starter/
│   └── README.md
└── README.md
```
