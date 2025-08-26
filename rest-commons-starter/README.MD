# rest-commons-starter (v1.0.1)

Стартер для Spring Boot, предоставляющий единую систему обработки исключений и форматирования ошибок для REST API.

## Возможности

- Единый формат ответов для всех ошибок API
- Автоматическая обработка исключений Spring (валидация, методы HTTP и т.д.)
- Поддержка кастомных исключений с детализированной информацией
- Автоконфигурация ObjectMapper с поддержкой Java Time API

## Подключение

```groovy
implementation "ru.digitalyard.commons:rest-commons-starter:1.0.1"
```

## Формат ответа об ошибке

Все ошибки возвращаются в едином формате:

```json
{
  "id": "7c9e6679-7425-40de-944b-e07fc1f90ae7",
  "status": 404,
  "code": "ENTITY_NOT_FOUND",
  "timestamp": "2025-08-25T12:34:56Z",
  "details": {
    "RESOURCE_TYPE": "User",
    "ID": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

## Возвращаемые коды ошибок
[CommonErrorCode.java](src/main/java/dy/digitalyard/commons/rest/exception/model/CommonErrorCode.java)

### 1. Не верный URL запроса
```json
{
  "id": "0b7ef482-7939-4bac-9d3d-d3ffabd94a60",
  "status": 404,
  "code": "NOT_FOUND",
  "timestamp": "2025-08-26T04:06:27.831779Z",
  "details": {
    "url": "/test/no-handler",
    "method": "GET"
  }
}
```

### 2. Отсутствует обязательный параметр запроса
```json
{
  "id": "a067383a-fe20-4f2d-8fb7-41f74f6912d0",
  "status": 400,
  "code": "REQUIRED_PARAMETER_MISSING",
  "timestamp": "2025-08-26T04:06:28.030777900Z",
  "details": {
    "parameterType": "String",
    "parameterName": "param"
  }
}
```

### 3. Параметр запроса или path variable не может быть преобразован в нужный тип
Пример: Ожидаем GET /api/user?id=123 (Integer), а вызывается GET /api/user?id=abc (String)
```json
{
  "id": "fcba33b8-b55c-47b9-a5f8-12ee3e5e4f2f",
  "status": 400,
  "code": "ARGUMENT_TYPE_MISMATCH",
  "timestamp": "2025-08-26T04:06:27.954776900Z",
  "details": {
    "value": "abc",
    "parameterName": "param",
    "requiredType": "Integer"
  }
}
```

### 4. Не проходит валидация тела запроса (@RequestBody)
Пример: над полем name в UserDto стоит аннотация @Size(min = 3, max = 20, message = "Имя должно быть от 3 до 20 символов"). @RequestBody используется с @Valid.
 - Ожидается 
```json
{
  "name": "Alice"
}
```
 - Приходит
```json
{
  "name": ""
}
```
Ответ:
```json
{
  "id": "44359e88-a65a-4ed9-924a-c15f5e61b1a2",
  "status": 400,
  "code": "VALIDATION_FAILED",
  "timestamp": "2025-08-26T04:06:27.995777600Z",
  "details": {
    "violations": {
      "name": [
        "Имя должно быть от 3 до 20 символов"
      ]
    }
  }
}
```

### 5. Не проходит валидация параметром запроса или path variable
Пример: У запроса 2 параметра: name и age. name не может быть пустым (@RequestParam @NotBlank String name), a age не может быть меньше 18 (@RequestParam @Min(18) int age)
Ожидаем: GET /api/user?name=Alice&age=20
Ошибка при: GET /api/user?name=&age=20 или GET /api/user?name=Alice&age=15

```json
{
  "id": "d9760570-b3c7-458c-9c0e-54b976706d6f",
  "status": 400,
  "code": "VALIDATION_FAILED",
  "timestamp": "2025-08-26T04:06:27.944778100Z",
  "details": {
    "violations": {
      "param": [
        "size must be between 4 and 2147483647"
      ]
    }
  }
}
```

### 6. Запрос с HTTP-методом, который не поддерживается для данного URL
```json
{
  "id": "92e34319-137e-4b25-876c-fd34c96eb953",
  "status": 405,
  "code": "METHOD_NOT_ALLOWED",
  "timestamp": "2025-08-26T04:06:27.553778300Z",
  "details": {
    "supportedMethods": [
      "POST"
    ]
  }
}
```

### 6. Не удалось распарсить тело запроса
 - Некорректный JSON (синтаксическая ошибка)
 - Типы не совпадают (например, в JSON строка, а в Java ожидается число)
 - Пустое тело при обязательном @RequestBody

```json
{
  "id": "81f0baaf-be21-4db3-8cd2-4e45a265a794",
  "code": "HTTP_MESSAGE_NOT_READABLE",
  "status": 400,
  "timestamp": "2025-08-26T07:02:57.004380400Z",
  "details": {
    "reason": "JSON parse error: Unrecognized field \"username\" (class dy.digitalyard.commons.rest.integration.handler.TestConfig$DummyUser), not marked as ignorable"
  }
}
```

### 7. Не поддерживаемый content-type
Пример: Ожидается application/json а вызывается с Content-Type: text/plain
```json
{
  "id": "5f55546a-2940-4a3e-8eac-0644f88474d6",
  "code": "UNSUPPORTED_MEDIA_TYPE",
  "status": 415,
  "timestamp": "2025-08-26T07:02:57.224376300Z",
  "details": {
    "contentType": "text/plain;charset=UTF-8",
    "supportedMediaTypes": [
      "application/json",
      "application/*+json"
    ]
  }
}
```

### 8. Сущность не найдена в базе данных
```json
{
  "id": "095a600e-c240-4b36-ac97-9f889fe7eba3",
  "code": "ENTITY_NOT_FOUND",
  "status": 404,
  "timestamp": "2025-08-26T07:02:57.165377800Z",
  "details": {
    "resourceType": "TestResource",
    "id": "1ddff930-596c-4d6d-89aa-f4a591d09a9e"
  }
}
```
### 9. Сервис не доступен
```json
{
  "id": "4c4a3c58-1412-492a-b029-43863ec3a76a",
  "code": "SERVICE_UNAVAILABLE",
  "status": 500,
  "timestamp": "2025-08-26T07:02:57.027378400Z",
  "details": {
    "serviceName": "TestService"
  }
}
```

### 10. Неизвестная ошибка
```json
{
  "id": "09ff6208-e9ab-4015-99f7-7481905be39b",
  "code": "UNKNOWN_INTERNAL_ERROR",
  "status": 500,
  "timestamp": "2025-08-26T07:02:57.036378300Z",
  "details": {}
}
```

## Настройка ObjectMapper

Стартер автоматически настраивает ObjectMapper с:
- Поддержкой Java Time API
- Отключением сериализации дат в timestamp
- Исключением null-полей из JSON
- Правильным форматированием дат в ISO-8601
