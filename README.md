# dy-commons

`dy-commons` — это набор Spring Boot стартеров, которые предоставляют общие функциональные возможности для сервисов Digital Yard.

## Доступные стартеры

### 1. [rest-commons-starter](rest-commons-starter/README.MD) (v1.0.2)

Стартер для Spring Boot, предоставляющий единую систему обработки исключений и форматирования ошибок для REST API.

**Возможности:**

- Единый формат ответов для всех ошибок API
- Автоматическая обработка исключений Spring (валидация, методы HTTP и т.д.)
- Поддержка кастомных исключений с детализированной информацией
- Автоконфигурация `ObjectMapper` с поддержкой Java Time API

---

### 2. [web-security-starter](web-security-starter/README.MD) (v1.0.2)

Стартер для Spring Boot, добавляющий поддержку JWT-аутентификации и базовых правил безопасности.

**Возможности:**

- Валидация JWT по публичному X.509 сертификату
- Извлечение пользователя и ролей из claims
- Фильтр `Authorization: Bearer ...`
- Единый формат ошибок через `rest-commons-starter`
- Настройка CORS и открытых эндпоинтов через `application.yaml`

---

### 3. [tracing-starter](tracing-starter/README.MD) (v1.0.2)

Стартер для Spring Boot, обеспечивающий единый механизм трассировки запросов в распределённых системах.

**Возможности:**

- Автоматическая генерация `traceId` и `spanId` для каждого HTTP-запроса
- Передача `traceId` через заголовки HTTP между сервисами
- Поддержка логирования всех входящих и исходящих запросов с привязкой к трассировке
- Интеграция с популярными системами мониторинга и трассировки (например, OpenTelemetry, Sleuth)
- Единый формат логов с указанием traceId и spanId


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
| ---------------------------- | ----------- | -------------------------------------------- |
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
| -------------- | ----------- | --------------------------------------- |
| UNAUTHORIZED   | 401         | Токен отсутствует                       |
| TOKEN\_EXPIRED | 401         | Токен просрочен                         |
| INVALID\_TOKEN | 401         | Неверная подпись токена                 |
| FORBIDDEN      | 403         | Недостаточно прав для доступа к ресурсу |

---

### JWT claims (из `web-security-starter`)

| Claim        | Описание                  |
| ------------ | ------------------------- |
| sub          | UUID пользователя         |
| LOGIN        | Логин пользователя        |
| FIRST\_NAME  | Имя пользователя          |
| MIDDLE\_NAME | Отчество пользователя     |
| LAST\_NAME   | Фамилия пользователя      |
| USER\_RIGHTS | Список ролей пользователя |

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
