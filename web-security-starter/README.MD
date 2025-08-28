# web-security-starter (v1.0.2)

Стартер для Spring Boot, добавляющий поддержку JWT-аутентификации и базовых правил безопасности.

## Содержание
1. [Возможности](#возможности)
2. [Подключение](#подключение)
3. [Настройка (application.yaml)](#настройка-applicationyaml)
    - [Обязательные параметры](#обязательные-параметры)
    - [Настройки аутентификации (dy.auth.*)](#настройки-аутентификации-dyauth)
    - [Настройки безопасности (dy.web-security.*)](#настройки-безопасности-dyweb-security)
    - [Пример настройки для разработки](#пример-настройки-для-разработки)
4. [Claims в JWT](#claims-в-jwt)
5. [Ошибки](#ошибки)
    - [1. Токен отсутствует при запросе защищенного ресурса](#1-токен-отсутствует-при-запросе-защищенного-ресурса)
    - [2. Токен просрочен](#2-токен-просрочен)
    - [3. Неверная подпись токена](#3-неверная-подпись-токена-подписан-другим-ключом)
    - [4. Недостаточно прав для доступа](#4-у-пользователя-недостаточно-прав-для-доступа-к-ресурсу)


## Возможности
- Валидация JWT по публичному X.509 сертификату
- Извлечение пользователя и ролей из claims
- Фильтр `Authorization: Bearer ...`
- Единый формат ошибок через `rest-commons-starter`
- Настройка CORS и открытых эндпоинтов через `application.yaml`

## Подключение
```groovy
implementation "ru.digitalyard.commons:web-security-starter:1.0.1"
```

## Настройка (application.yaml)
# Конфигурация безопасности

## Обязательные параметры
- `dy.auth.public-key-path` - путь к PEM-файлу с публичным ключом для верификации JWT-токенов

## Настройки аутентификации (`dy.auth.*`)
| Параметр | Обязательный | Описание |
|----------|--------------|----------|
| `public-key-path` | Да | Путь к файлу публичного ключа (X.509). Поддерживает форматы `classpath:`, `file:`, `http:` |

## Настройки безопасности (`dy.web-security.*`)
| Параметр | Обязательный | По умолчанию | Описание |
|----------|--------------|--------------|----------|
| `permissions` | Нет | `[]` | Массив URL-шаблонов, доступных без аутентификации |
| `cors.allowed-origins` | Нет | `["*"]` | Разрешенные домены для CORS |
| `cors.allowed-methods` | Нет | `["GET", "POST", "PUT", "DELETE", "OPTIONS"]` | Разрешенные HTTP-методы |
| `cors.allowed-headers` | Нет | `["*"]` | Разрешенные заголовки запроса |
| `cors.allow-credentials` | Нет | `false` | Разрешение передачи cookies |
| `cors.max-age` | Нет | `1h` | Время кэширования CORS-префлайт запросов |

## Пример настройки для разработки
```yaml
dy:
  auth:
    public-key-path: classpath:dev_public.pem
  web-security:
    permissions:
      - "/actuator/health"
      - "/v3/api-docs/**"
    cors:
      allowed-origins: "*"
      allowed-methods: "*"
      allow-credentials: false
```
## Claims в JWT
 - sub — UUID пользователя
 - LOGIN — логин
 - FIRST_NAME, MIDDLE_NAME, LAST_NAME
 - USER_RIGHTS — список ролей

## Ошибки
### 1. Токен отсутствует при запросе защищенного ресурса
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 401,
    "code": "UNAUTHORIZED",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "unauthorized": "Full authentication is required to access this resource"
    }
   }
```
### 2. Токен просрочен
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 401,
    "code": "TOKEN_EXPIRED",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "expiredAt": "2025-08-25T05:30:16Z"
    }
   }
```
### 3. Неверная подпись токена (подписан другим ключом)
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 401,
    "code": "INVALID_TOKEN",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "reason": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."
    }
   }
```
### 4. У пользователя недостаточно прав для доступа к ресурсу
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 403,
    "code": "FORBIDDEN",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "url": "/test/admin",
      "login": "mock_login"
    }
   }
```
