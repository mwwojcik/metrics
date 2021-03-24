package mw.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MetricRegistry metricRegistry;
    private Map<String, String> cache = new HashMap<>();

    @PostConstruct
    public void registerMetric() {
        metricRegistry.register("CACHE_SIZE", (Gauge<Integer>) () -> cache.size());
    }

    @Scheduled(fixedDelay = 3000)
    public void scheduleFixedDelayTask() {
        System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
        getFor("");
        score("");
    }

    @Timed(name="getForName",absolute = true)
    public String getFor(String name) {

       // try {

            cache.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());


     /*   } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }*/
        return "Test";
    }

    @Timed(name="score",absolute = true)
    public String score(String name) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
        return name;
    }

}
