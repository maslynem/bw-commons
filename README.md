# dy-commons

`dy-commons` — это набор Spring Boot стартеров, которые предоставляют общие функциональные возможности для сервисов Digital Yard.

## Доступные стартеры

### 1. [rest-commons-starter](rest-commons-starter/README.md) (v1.0.1)

Стартер для Spring Boot, предоставляющий единую систему обработки исключений и форматирования ошибок для REST API.

**Возможности:**

- Единый формат ответов для всех ошибок API
- Автоматическая обработка исключений Spring (валидация, методы HTTP и т.д.)
- Поддержка кастомных исключений с детализированной информацией
- Автоконфигурация `ObjectMapper` с поддержкой Java Time API

---

### 2. [web-security-starter](web-security-starter/README.md) (v1.0.1)

Стартер для Spring Boot, добавляющий поддержку JWT-аутентификации и базовых правил безопасности.

**Возможности:**

- Валидация JWT по публичному X.509 сертификату
- Извлечение пользователя и ролей из claims
- Фильтр `Authorization: Bearer ...`
- Единый формат ошибок через `rest-commons-starter`
- Настройка CORS и открытых эндпоинтов через `application.yaml`

---

## Как подключить

Каждый стартер подключается через Gradle/Maven по отдельности:

```groovy
implementation "ru.digitalyard.commons:rest-commons-starter:1.0.1"
implementation "ru.digitalyard.commons:web-security-starter:1.0.1"
```