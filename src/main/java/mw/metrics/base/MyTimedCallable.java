package mw.metrics.base;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.Callable;

public class MyTimedCallable<V> implements Callable<V> {
    private final MeterRegistry registry;
    private final Timer executionTimer;
    private final Timer idleTimer;
    private final Callable<V> callable;
    private final Timer.Sample idleSample;

    MyTimedCallable(MeterRegistry registry, Timer executionTimer, Timer idleTimer, Callable<V> callable) {
        this.registry = registry;
        this.executionTimer = executionTimer;
        this.idleTimer = idleTimer;
        this.callable = callable;
        this.idleSample = Timer.start(registry);
    }

    @Override
    public V call() throws Exception {
        idleSample.stop(idleTimer);
        Timer.Sample executionSample = Timer.start(registry);
        try {
            return callable.call();
        } finally {
            executionSample.stop(executionTimer);
        }
    }
}
