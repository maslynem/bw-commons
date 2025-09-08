package ru.boardworld.commons.tracing;

import ru.boardworld.commons.tracing.thread.ThreadPoolTaskExecutorPostProcessor;
import ru.boardworld.commons.utils.yaml.CustomConfigPropertiesReaderFactory;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@AutoConfiguration
@ConditionalOnClass(io.micrometer.tracing.Tracer.class)
@PropertySource(value = "classpath:tracing-config.yaml", factory = CustomConfigPropertiesReaderFactory.class)
public class TracingAutoConfiguration {

    // BeanPostProcessor для ThreadPoolTaskExecutor
    @Bean
    @ConditionalOnMissingBean(name = "tracerThreadPoolTaskExecutorPostProcessor")
    public static ThreadPoolTaskExecutorPostProcessor tracerThreadPoolTaskExecutorPostProcessor(
            ObjectProvider<Tracer> tracerProvider) {
        return new ThreadPoolTaskExecutorPostProcessor(tracerProvider);
    }
}
