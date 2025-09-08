package ru.boardworld.commons.rest.exception.mapper;

import ru.boardworld.commons.rest.exception.exception.AbstractApiException;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.details.EmptyDetails;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExceptionDetailsMapperRegistry {
    private final List<ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails>> mappers;
    private final Map<Class<? extends Exception>, ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails>> byExactType;

    public ExceptionDetailsMapperRegistry(List<ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails>> mappers) {
        this.mappers = List.copyOf(mappers);
        this.byExactType = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(ExceptionDetailsMapper::canHandle, m -> m));
    }

    public ApiErrorDetails map(Exception ex) {
        if (ex == null) {
            return null;
        }
        // 1) если это наш собственный AbstractApiException — взять уже готовые details
        if (ex instanceof AbstractApiException) {
            return ((AbstractApiException) ex).getDetails();
        }
        // 2) точное совпадение класса
        ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails> exact = byExactType.get(ex.getClass());
        if (exact != null) {
            return mapWithUnchecked(exact, ex);
        }
        // 3) выбрать наиболее специфичный маппер по assignability (минимальная дистанция наследования)
        Optional<ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails>> assignable = mappers.stream()
                .filter(m -> m.supports(ex))
                .min(Comparator.comparingInt(m -> inheritanceDistance(m.canHandle(), ex.getClass()))); // минимальная дистанция = наиболее специфичный
        return assignable
                .map(m -> {
                    ApiErrorDetails res = mapWithUnchecked(m, ex);
                    return res == null ? EmptyDetails.INSTANCE : res;
                })
                .orElse(EmptyDetails.INSTANCE);
    }

    @SuppressWarnings("unchecked")
    private <E extends Exception, D extends ApiErrorDetails> D mapWithUnchecked(ExceptionDetailsMapper<E, D> mapper, Exception ex) {
        return mapper.mapToDetails((E) ex);
    }

    /**
     * Вычисляет расстояние наследования от baseClass до derivedClass (по цепочке superclasses).
     * Если класс не является потомком — возвращает Integer.MAX_VALUE.
     */
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
