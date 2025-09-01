package dy.digitalyard.commons.tracing.thread;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(io.micrometer.tracing.Tracer.class)
public class ThreadPoolTaskExecutorPostProcessor implements BeanPostProcessor {

    private final ObjectProvider<Tracer> tracerProvider;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean,
                                                 @NonNull String beanName) {
        if (!(bean instanceof ThreadPoolTaskExecutor tpe)) {
            return bean;
        }

        try {
            // прочитаем приватное поле taskDecorator, если оно есть
            TaskDecorator existing = readExistingTaskDecorator(tpe);
            TaskDecorator ours = new MdcTaskDecorator(tracerProvider);

            TaskDecorator wrapped;
            if (existing != null) {
                // оборачиваем: сначала existing.decorate, затем наш контекст
                wrapped = runnable -> {
                    Runnable r = existing.decorate(runnable);
                    return ours.decorate(r);
                };
            } else {
                wrapped = ours;
            }
            tpe.setTaskDecorator(wrapped);
            log.debug("Set wrapped TaskDecorator for ThreadPoolTaskExecutor bean '{}'", beanName);
        } catch (Throwable ex) {
            log.warn("Failed to wrap TaskDecorator for ThreadPoolTaskExecutor '{}'. Setting our TaskDecorator directly. Reason: {}",
                    beanName, ex.toString());
            try {
                tpe.setTaskDecorator(new MdcTaskDecorator(tracerProvider));
            } catch (Throwable t) {
                log.error("Failed to set TaskDecorator on ThreadPoolTaskExecutor '{}': {}", beanName, t.toString());
            }
        }

        return bean;
    }

    private TaskDecorator readExistingTaskDecorator(ThreadPoolTaskExecutor tpe) {
        try {
            Field f = ThreadPoolTaskExecutor.class.getDeclaredField("taskDecorator");
            f.setAccessible(true);
            Object val = f.get(tpe);
            if (val instanceof TaskDecorator td) return td;
        } catch (NoSuchFieldException e) {
            log.debug("ThreadPoolTaskExecutor has no 'taskDecorator' field (or field not accessible).");
        } catch (Throwable t) {
            log.debug("Unable to read existing taskDecorator via reflection: {}", t.toString());
        }
        return null;
    }
}
