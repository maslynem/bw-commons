package dy.digitalyard.commons.tracing;

import dy.digitalyard.commons.tracing.thread.ThreadPoolTaskExecutorPostProcessor;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

@AutoConfiguration
@ConditionalOnClass(Tracer.class)
public class TracingAutoConfiguration {

    // Регистрируем фильтр только в servlet-приложениях и только если есть Tracer
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(name = "tracingMdcFilterRegistration")
    public FilterRegistrationBean<OncePerRequestFilter> tracingMdcFilterRegistration(Tracer tracer) {
        TracingMdcFilter filter = new TracingMdcFilter(tracer);
        FilterRegistrationBean<OncePerRequestFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.LOWEST_PRECEDENCE - 100);
        reg.setName("tracingMdcFilter");
        return reg;
    }

    // BeanPostProcessor для ThreadPoolTaskExecutor
    @Bean
    @ConditionalOnMissingBean(name = "tracerThreadPoolTaskExecutorPostProcessor")
    public ThreadPoolTaskExecutorPostProcessor tracerThreadPoolTaskExecutorPostProcessor(Tracer tracerProvider) {
        return new ThreadPoolTaskExecutorPostProcessor(tracerProvider);
    }
}
