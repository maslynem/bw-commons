package com.digitalyard.commons.rest.exception.mapper;

import com.digitalyard.commons.rest.exception.exception.AbstractApiException;

import java.util.*;
import java.util.stream.Collectors;

public class ExceptionDetailsMapperRegistry {

    private final List<ExceptionDetailsMapper<? extends Exception>> mappers;
    private final Map<Class<? extends Exception>, ExceptionDetailsMapper<? extends Exception>> byExactType;

    public ExceptionDetailsMapperRegistry(List<ExceptionDetailsMapper<? extends Exception>> mappers) {
        this.mappers = List.copyOf(mappers);
        this.byExactType = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(ExceptionDetailsMapper::canHandle, m -> m));
    }

    /**
     * Возвращает мапу details для исключения. Никогда не бросает ClassCastException в вызывающем коде.
     */
    public Map<String, Object> map(Exception ex) {
        if (ex == null) {
            return Collections.emptyMap();
        }

        // 1) если это наш собственный AbstractApiException — взять уже готовые details
        if (ex instanceof AbstractApiException) {
            return ((AbstractApiException)ex).getDetails();
        }

        // 2) точное совпадение класса
        ExceptionDetailsMapper<? extends Exception> exact = byExactType.get(ex.getClass());
        if (exact != null) {
            return mapWithUnchecked(exact, ex);
        }

        // 3) выбрать наиболее специфичный маппер по assignability
        Optional<ExceptionDetailsMapper<? extends Exception>> assignable = mappers.stream()
                .filter(m -> m.supports(ex))
                .min(Comparator.comparingInt(m -> inheritanceDistance(m.canHandle(), ex.getClass()))); // минимальная дистанция = наиболее специфичный

        return assignable.map(m -> mapWithUnchecked(m, ex)).orElse(Collections.emptyMap());
    }

    @SuppressWarnings("unchecked")
    private <E extends Exception> Map<String, Object> mapWithUnchecked(ExceptionDetailsMapper<E> mapper, Exception ex) {
        return mapper.mapToDetails((E) ex);
    }

    private static int inheritanceDistance(Class<?> baseClass, Class<?> derivedClass) {
        int dist = 0;
        Class<?> cls = derivedClass;
        while (cls != null && !baseClass.equals(cls)) {
            cls = cls.getSuperclass();
            dist++;
        }
        return cls == null ? Integer.MAX_VALUE : dist;
    }
}
