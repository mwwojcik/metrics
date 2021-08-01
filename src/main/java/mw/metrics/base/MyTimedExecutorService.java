package mw.metrics.base;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

/**
 * An {@link java.util.concurrent.ExecutorService} that is timed. This class is for internal use.
 *
 * @author Jon Schneider
 * @see io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics
 */
public class MyTimedExecutorService implements ExecutorService {
    private final MeterRegistry registry;
    private final ExecutorService delegate;
    private final Timer executionTimer;
    private final Timer idleTimer;

    public MyTimedExecutorService(MeterRegistry registry, ExecutorService delegate, String executorServiceName,
                                String metricPrefix, Iterable<Tag> tags) {
        this.registry = registry;
        this.delegate = delegate;
        Tags finalTags = Tags.concat(tags, "name", executorServiceName);
        this.executionTimer = registry.timer(metricPrefix + "executor", finalTags);
        this.idleTimer =
            Timer.builder(metricPrefix + "executor.idle").tags(finalTags).publishPercentiles(0.1,0.25,0.5,0.75,0.9,0.95,0.99).register(registry);
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(wrap(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(wrap(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(wrap(task));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return delegate.invokeAll(wrapAll(tasks));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.invokeAll(wrapAll(tasks), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return delegate.invokeAny(wrapAll(tasks));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.invokeAny(wrapAll(tasks), timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        delegate.execute(wrap(command));
    }

    private Runnable wrap(Runnable task) {
        return new MyTimedRunnable(registry, executionTimer, idleTimer, task);
    }

    private <T> Callable<T> wrap(Callable<T> task) {
        return new MyTimedCallable<>(registry, executionTimer, idleTimer, task);
    }

    private <T> Collection<? extends Callable<T>> wrapAll(Collection<? extends Callable<T>> tasks) {
        return tasks.stream().map(this::wrap).collect(toList());
    }
}