# web-security-starter (v1.0.1)

Стартер для Spring Boot, добавляющий поддержку JWT-аутентификации и базовых правил безопасности.

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

### Минимально
```yaml
dy:
  auth:
    public-key-path: classpath:public.crt   # X.509 сертификат
  web-security:
    permissions:
      - /actuator/health
    cors:
      allowed-origins:
        - "http://localhost:3000"
        - "https://example.com"
```

## Claims в JWT
 - sub — UUID пользователя
 - LOGIN — логин
 - FIRST_NAME, MIDDLE_NAME, LAST_NAME
 - USER_RIGHTS — список ролей
 - LOCKED, DELETED — флаги блокировки/удаления

## Ошибки
1. Токен отсутствует при запросе защищенного ресурса
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
2. Токен просрочен
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
3. Неверная подпись токена (подписан другим ключом)
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
4. Пользователь заблокирован
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 403,
    "code": "BLOCKED_USER",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "login": "USER_LOGIN"
    }
   }
```
5. Пользователь удален
```json
   {
    "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
    "status": 401,
    "code": "USER_DELETED_OR_DOES_NOT_EXIST",
    "timestamp": "2025-08-25T12:34:56Z",
    "details": {
      "login": "userLogin"
    }
   }
```
6. У пользователя недостаточно прав для доступа к ресурсу
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
