package dy.digitalyard.commons.tracing;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.MDC;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AutoConfiguration
@ConditionalOnClass(Tracer.class)
public class TracingAutoConfiguration {

    /**
     * BeanPostProcessor, который автоматически выставляет TaskDecorator в ThreadPoolTaskExecutor'ы,
     * если такие beans присутствуют в контексте.
     */
    @Component
    public static class ThreadPoolTaskExecutorPostProcessor implements BeanPostProcessor {

        private final Tracer tracer;

        public ThreadPoolTaskExecutorPostProcessor(Tracer tracer) {
            this.tracer = tracer;
        }

        @Override
        public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) {
            if (bean instanceof ThreadPoolTaskExecutor tpe) {
                tpe.setTaskDecorator(new MdcTaskDecorator(tracer));
            }
            return bean;
        }
    }


    /**
     * TaskDecorator для ThreadPoolTaskExecutor — чтобы при выполнении задач
     * в потоках также сохранялись traceId/spanId в MDC.
     */
    public static class MdcTaskDecorator implements TaskDecorator {
        private final Tracer tracer;

        public MdcTaskDecorator(Tracer tracer) {
            this.tracer = tracer;
        }

        @Override
        public @NonNull Runnable decorate(@NonNull Runnable runnable) {
            Span current = tracer.currentSpan();
            final String traceId = current != null && current.context() != null ? current.context().traceId() : null;
            final String spanId = current != null && current.context() != null ? current.context().spanId() : null;

            return () -> {
                try {
                    if (traceId != null) MDC.put("traceId", traceId);
                    if (spanId != null) MDC.put("spanId", spanId);
                    runnable.run();
                } finally {
                    MDC.remove("traceId");
                    MDC.remove("spanId");
                }
            };
        }
    }
}
