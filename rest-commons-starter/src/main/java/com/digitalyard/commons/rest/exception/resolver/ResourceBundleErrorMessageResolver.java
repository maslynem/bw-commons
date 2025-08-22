package com.digitalyard.commons.rest.exception.resolver;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;

/**
 * Реализация ErrorMessageResolver на основе Spring MessageSource.
 * Поддерживает локализацию и параметризацию сообщений.
 */
@Slf4j
public class ResourceBundleErrorMessageResolver implements ErrorMessageResolver {

    private final MessageSource messageSource;
    private final Locale defaultLocale;

    public ResourceBundleErrorMessageResolver(MessageSource messageSource) {
        this(messageSource, Locale.getDefault());
    }

    public ResourceBundleErrorMessageResolver(MessageSource messageSource, Locale defaultLocale) {
        this.messageSource = messageSource;
        this.defaultLocale = defaultLocale;
    }

    @Override
    public String resolve(ErrorCode errorCode, Map<String, Object> details) {
        return resolve(errorCode, details, defaultLocale);
    }

    @Override
    public String resolve(ErrorCode errorCode, Map<String, Object> details, Locale locale) {
        try {
            Object[] args = prepareArguments(details);

            return messageSource.getMessage(
                    errorCode.getCode(),
                    args,
                    "Error message not found for code: " + errorCode.getCode(),
                    locale
            );
        } catch (Exception e) {
            log.error("Can't create error message for errorCode {}", errorCode, e);
            return errorCode.getCode();
        }
    }

    private Object[] prepareArguments(Map<String, Object> details) {
        if (details == null || details.isEmpty()) {
            return new Object[0];
        }
        return details.values().toArray();
    }

}