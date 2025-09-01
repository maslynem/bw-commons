# Tracing Starter

Tracing Starter — это Spring Boot авто-конфигурируемый модуль, предназначенный для централизации логики отправки метрик,
логов и трейсов в системы мониторинга, такие как Grafana Alloy (или другие коллекторы, поддерживающие OTLP). Он
интегрирует Micrometer Tracing с OpenTelemetry (OTEL) bridge, настраивает экспорт через OTLP (gRPC по умолчанию).

## Содержание

- [Возможности](#возможности)
- [Подключение](#подключение)
- [Трейсы](#трейсы)
- [Метрики](#метрики)
- [Логирование](#логирование)
- [Переменные окружения](#переменные-окружения)
- [ThreadPoolTaskExecutor](#threadpooltaskexecutor)

## Возможности

- **Трейсинг**: Автоматическая настройка Micrometer Tracing с мостом к OpenTelemetry. Экспорт трейсов в OTLP-эндпоинт (gRPC/HTTP).
- **Метрики**: Экспорт метрик через Micrometer OTLP registry (HTTP).
- **Логи**: Интеграция с Logback для отправки логов в OTEL через OpenTelemetry Log Appender (gRPC/HTTP).

## Подключение

   ```
   dependencies {
       implementation 'ru.digitalyard.commons:tracing-starter:latest'
   }
   ```

## Трейсы
Трейсы отправляются в OTeL Collector по gRPC/HTTP.

## Метрики
Метрики можно обрабатывать следующими способами:
 - Отправлять в OTeL Collector по gRPC/HTTP (поведение по умолчанию)
 - Предоставить endpoint /actuator/prometheus для активного scratch. (Для этого необходимо OTLP_METRICS_ENABLED=false, PROMETHEUS_METRICS_EXPORT_ENABLED=true)

## Логирование
Настроено через `logback-spring.xml`:
Предоставлено 3 appender:

- **ConsoleAppender**: Вывод в консоль с паттерном, включающим timestamp, level, logger, message, traceId, spanId и
  exception.
- **OpenTelemetryAppender**: Отправляет логи в OTEL SDK (экспорт в OTLP).
- **JsonFileAppender**: Вывод логов в файл в json формате. Включает поле app, timestamp, logLevel, loggerName, message,
  mdc (включая traceId и spanId) и stackTrace.

Логи можно обрабатывать следующими способами:
 - Отправлять в OTeL Collector по gRPC/HTTP по протоколу OTLP
 - Записывать логи в файл в json формате

### Переменные окружения

Основные настраиваемые переменные:

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

## ThreadPoolTaskExecutor

`ThreadPoolTaskExecutorPostProcessor` — BeanPostProcessor, применяемый ко всем ThreadPoolTaskExecutor бинам:

- Захватывает существующий TaskDecorator (если есть) и оборачивает его.
- Добавляет `MdcTaskDecorator`: При submit задачи захватывает текущий MDC, traceId и spanId.
- В worker-потоке восстанавливает MDC и IDs для логов в асинхронных задачах.
- Не пропагатирует полный Span (только IDs для логов; для полного трейсинга используйте Tracer вручную).
- Логирует ошибки, если рефлексия не удалась.

Это предотвращает потерю контекста в @Async методах или custom пулах.