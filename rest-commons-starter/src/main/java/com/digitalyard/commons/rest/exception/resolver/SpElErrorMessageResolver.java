package com.digitalyard.commons.rest.exception.resolver;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class SpElErrorMessageResolver implements ErrorMessageResolver {
    private final MessageSource messageSource;
    private final Locale defaultLocale;
    private final ExpressionParser parser;
    private final ParserContext parserContext;


    @Override
    public String resolve(ErrorCode errorCode, Map<String, Object> details) {
        return resolve(errorCode, details, defaultLocale);
    }

    @Override
    public String resolve(ErrorCode errorCode, Map<String, Object> details, Locale locale) {
        try {
            // Получаем шаблон сообщения
            String messageTemplate = messageSource.getMessage(
                    errorCode.getCode(),
                    new Object[0],
                    "Error message not found for code: " + errorCode.getCode(),
                    locale
            );

            // Если нет деталей или шаблон не содержит выражений, возвращаем как есть
            if (details == null || details.isEmpty() || !messageTemplate.contains("#{")) {
                return messageTemplate;
            }

            // Создаем контекст SpEL и добавляем все детали как переменные
            StandardEvaluationContext context = new StandardEvaluationContext();
            details.forEach(context::setVariable);

            return parser.parseExpression(messageTemplate, parserContext)
                    .getValue(context, String.class);
        } catch (Exception e) {
            log.error("Can't create error message for errorCode {}", errorCode, e);
            return errorCode.getCode();
        }
    }
}