# tracing-starter (v1.0.2)

Стартер для Spring Boot, добавляющий поддержку трассировки запросов и логирования с traceId/spanId через Micrometer и
OpenTelemetry.

## Содержание

- [Возможности](#возможности)
- [Подключение](#подключение)
- [Основные параметры](#основные-параметры)
- [Логирование](#логирование)
- [Настройка логирования (Logback)](#настройка-логирования-logback)
- [Переменные среды](#переменные-среды)
- [MDC фильтр](#mdc-фильтр)
- [ThreadPoolTaskExecutor](#threadpooltaskexecutor)

## Возможности

- Автоматическая интеграция с Micrometer Tracing и OpenTelemetry
- Поддержка MDC для traceId/spanId в логах
- Асинхронный logging (JSON файл + консоль)
- BeanPostProcessor для ThreadPoolTaskExecutor с сохранением MDC контекста
- Настройка экспорта метрик в Prometheus (опционально)

## Подключение

```groovy
implementation "ru.digitalyard.commons:tracing-starter:1.0.2"
````

## Основные параметры

| Параметр                                       | По умолчанию                      | Описание                                           |
|------------------------------------------------|-----------------------------------|----------------------------------------------------|
| `management.prometheus.metrics.export.enabled` | `true`                            | Включение/выключение экспорта метрик Prometheus    |
| `management.endpoints.web.exposure.include`    | `health,info,prometheus`          | Список открытых эндпоинтов для actuator            |
| `management.tracing.sampling.probability`      | `1`                               | Вероятность сэмплирования трассировки (0..1)       |
| `management.otlp.tracing.endpoint`             | `http://localhost:4318/v1/traces` | URL OTLP collector для отправки данных трассировки |

## Логирование

* JSON-лог с полями:

    * `app` — имя приложения
    * `timestamp` — время события
    * `level` — уровень логирования
    * `class` — класс логирования
    * `traceId`, `spanId` — из MDC
    * `message` — текст сообщения
    * `exception` — стек ошибки (если есть)
* Асинхронная запись в файл (`/var/app-logs/app.log` по умолчанию)
* Консольный вывод удобен для разработки

## Настройка логирования (Logback)

`tracing-starter` использует `logback-spring.xml` для конфигурации логирования с поддержкой MDC (traceId/spanId).  
Вы можете управлять путями и ротацией логов через стандартные Spring свойства:

| Свойство                          | Назначение                             | По умолчанию                                 |
|-----------------------------------|----------------------------------------|----------------------------------------------|
| `logging.file.path`               | Путь к каталогу для файлов логов       | `/var/app-logs`                              |
| `logging.file.pattern`            | Паттерн имени файла для ротации        | `/var/app-logs/app.%d{yyyy-MM-dd}.%i.log.gz` |
| `logging.rotation.max-file-size`  | Максимальный размер одного файла логов | `50MB`                                       |
| `logging.rotation.max-history`    | Количество старых файлов для хранения  | `14`                                         |
| `logging.rotation.total-size-cap` | Общий размер всех файлов логов         | `5GB`                                        |

## Переменные среды

#### `ENDPOINTS_WEB_EXPOSURE_INCLUDE`

* **Назначение:** Определяет, какие Spring Boot Actuator эндпоинты будут открыты для HTTP-доступа.
* **Spring свойство:** `management.endpoints.web.exposure.include`
* **Детали:**
    * Список эндпоинтов через запятую (`health,info,prometheus`).
    * По умолчанию открыты `health`, `info` и `prometheus`.

---

#### `TRACING_SAMPLING_PROBABILITY`

* **Назначение:** Определяет вероятность сэмплирования запросов для трассировки.
* **Spring свойство:** `management.tracing.sampling.probability`
* **Детали:**
    * Значение от `0` до `1`.
        * `0` — трассировка полностью отключена.
        * `1` — трассируются все запросы.
        * `0.1` — трассируется 10% запросов.
    * Позволяет контролировать нагрузку на систему при сборе трассировок.

---

#### `OTLP_TRACING_ENDPOINT`

* **Назначение:** URL OTLP-коллектора для отправки данных трассировки.
* **Spring свойство:** `management.otlp.tracing.endpoint`
* **Детали:**
    * Указывает на OpenTelemetry Collector или Jaeger/Tempo endpoint.
    * Протокол HTTP/HTTPS
    * Если переменная не задана, по умолчанию используется `http://localhost:4318/v1/traces`.

---
#### `LOGGING_FILE_PATH`
* **Назначение:** Путь к каталогу для файлов логов.
* **Spring свойство:** `logging.file.path`
* **По умолчанию:** `/var/app-logs`

#### `LOGGING_FILE_PATTERN`

* **Назначение:** Паттерн имени файла для ротации логов.
* **Spring свойство:** `logging.file.pattern`
* **По умолчанию:** `/var/app-logs/app.%d{yyyy-MM-dd}.%i.log.gz`

#### `LOGGING_ROTATION_MAX_FILE_SIZE`

* **Назначение:** Максимальный размер одного файла логов.
* **Spring свойство:** `logging.rotation.max-file-size`
* **По умолчанию:** `50MB`


#### `LOGGING_ROTATION_MAX_HISTORY`

* **Назначение:** Количество старых файлов для хранения.
* **Spring свойство:** `logging.rotation.max-history`
* **По умолчанию:** `14`


#### `LOGGING_ROTATION_TOTAL_SIZE_CAP`
* **Назначение:** Общий размер всех файлов логов.
* **Spring свойство:** `logging.rotation.total-size-cap`
* **По умолчанию:** `5GB`


## MDC фильтр

`TracingMdcFilter` автоматически подставляет в MDC текущие `traceId` и `spanId` для каждого HTTP-запроса.

## ThreadPoolTaskExecutor

* Автоматическое декорирование задач с сохранением MDC контекста
* Поддержка уже существующих TaskDecorator через обёртку
* BeanPostProcessor: `ThreadPoolTaskExecutorPostProcessor`
