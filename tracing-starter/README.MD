# Tracing Starter

Tracing Starter — это Spring Boot авто-конфигурируемый модуль, предназначенный для централизации логики отправки метрик, логов и трейсов в системы мониторинга, такие как Grafana Alloy (или другие коллекторы, поддерживающие OTLP). Он интегрирует Micrometer Tracing с OpenTelemetry (OTEL) bridge, настраивает экспорт через OTLP (gRPC по умолчанию), добавляет обработку MDC для trace/span ID в логах и обеспечивает пропагation контекста в асинхронных задачах.

## Содержание

- [Возможности](#возможности)
- [Подключение](#подключение)
- [Основные параметры](#основные-параметры)
- [Логирование](#логирование)
- [Переменные среды](#переменные-среды)
- [MDC фильтр](#mdc-фильтр)
- [ThreadPoolTaskExecutor](#threadpooltaskexecutor)

## Возможности

- **Трейсинг**: Автоматическая настройка Micrometer Tracing с мостом к OpenTelemetry. Экспорт трейсов в OTLP-эндпоинт (gRPC).
- **Метрики**: Экспорт метрик через Micrometer OTLP registry (отключен Prometheus по умолчанию).
- **Логи**: Интеграция с Logback для отправки логов в OTEL через OpenTelemetry Log Appender (экспорт в OTLP).
- **MDC Propagation**: Автоматическое добавление traceId и spanId в MDC для веб-запросов и асинхронных задач, чтобы логи содержали контекст трейсинга.
- **Конфигурация по умолчанию**: Загружает `tracing-config.yaml` и `logback-spring.xml` для базовой настройки, с возможностью переопределения через application.properties или env vars.


## Подключение
   ```
   dependencies {
       implementation 'ru.digitalyard.commons:tracing-starter:1.0.2-SNAPSHOT'
   }
   ```

## Основные параметры

Параметры загружаются из `tracing-config.yaml` и могут быть переопределены в application.properties/yaml или env vars:

- `management.tracing.sampling.probability`: Вероятность семплинга трейсов (default: 1.0 — все трейсы).
- `management.otlp.tracing.endpoint`: Эндпоинт для экспорта трейсов (default: http://localhost:4317 — gRPC).
- `management.otlp.metrics.export.url`: Эндпоинт для экспорта метрик (default: http://localhost:4317).
- `management.otlp.metrics.export.enabled`: Включить экспорт метрик (default: true).
- `management.endpoints.web.exposure.include`: Экспонируемые эндпоинты Actuator (default: health,info).
- `management.prometheus.metrics.export.enabled`: Отключен по умолчанию (false), чтобы избежать дублирования с OTLP.

Для логов: Уровень логирования root — INFO, но может быть изменён в вашем logback.xml.

## Логирование

Настроено через `logback-spring.xml`:
- **ConsoleAppender**: Вывод в консоль с паттерном, включающим timestamp, level, logger, message, traceId, spanId и exception.
- **OpenTelemetryAppender**: Отправляет логи в OTEL SDK (экспорт в OTLP). Захватывает все MDC атрибуты (включая traceId/spanId).
- **AsyncAppender**: Оборачивает OTEL appender для асинхронной отправки (queueSize=8192, без discarding).

Логи экспортируются в тот же OTLP эндпоинт, что и трейсы (через глобальный OTEL SDK). Для кастомизации добавьте свой logback.xml и импортируйте/переопределите appenders.

## Переменные среды

Стартер поддерживает переопределение через env vars (Spring Boot convention):
- `TRACING_SAMPLING_PROBABILITY`: Аналог management.tracing.sampling.probability.
- `OTLP_TRACING_ENDPOINT`: Эндпоинт трейсов.
- `OTLP_METRICS_ENDPOINT`: Эндпоинт метрик.
- `ENDPOINTS_WEB_EXPOSURE_INCLUDE`: Экспонируемые эндпоинты.
- OTEL-специфичные vars (например, OTEL_SERVICE_NAME) для глобальной конфигурации OTEL SDK.


## MDC фильтр

`TracingMdcFilter` — OncePerRequestFilter, регистрируемый в авто-конфигурации:
- Для каждого HTTP-запроса извлекает currentSpan из Tracer.
- Добавляет `traceId` и `spanId` в SLF4J MDC.
- Удаляет их после обработки запроса.
- Порядок: LOWEST_PRECEDENCE - 100 (после других фильтров).
- Условие: Только для servlet web-apps и если есть Tracer.

Это обеспечивает, что все логи в рамках запроса содержат трейс-контекст.

## ThreadPoolTaskExecutor

`ThreadPoolTaskExecutorPostProcessor` — BeanPostProcessor, применяемый ко всем ThreadPoolTaskExecutor бинам:
- Захватывает существующий TaskDecorator (если есть) и оборачивает его.
- Добавляет `MdcTaskDecorator`: При submit задачи захватывает текущий MDC, traceId и spanId.
- В worker-потоке восстанавливает MDC и IDs для логов в асинхронных задачах.
- Не пропагатирует полный Span (только IDs для логов; для полного трейсинга используйте Tracer вручную).
- Логирует ошибки, если рефлексия не удалась.

Это предотвращает потерю контекста в @Async методах или custom пулах.