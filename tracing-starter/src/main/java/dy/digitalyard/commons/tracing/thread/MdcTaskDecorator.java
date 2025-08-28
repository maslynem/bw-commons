package dy.digitalyard.commons.tracing.thread;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * Копирует всю MDC карту (если есть) и добавляет traceId/spanId из текущего span (если есть).
 * Декоратор применяется в момент submit'а задачи.
 */
@RequiredArgsConstructor
public class MdcTaskDecorator implements TaskDecorator {

    private final ObjectProvider<Tracer> tracerProvider;

    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {
        Tracer tracer = tracerProvider.getIfAvailable();
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        Span current = tracer != null ? tracer.currentSpan() : null;
        final String traceId = (current != null && current.context() != null) ? current.context().traceId() : null;
        final String spanId = (current != null && current.context() != null) ? current.context().spanId() : null;

        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                } else {
                    MDC.clear();
                }
                if (traceId != null) MDC.put("traceId", traceId);
                if (spanId != null) MDC.put("spanId", spanId);

                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
