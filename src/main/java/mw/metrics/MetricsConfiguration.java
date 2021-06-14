package mw.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MetricsConfiguration {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Configuration
    public static class MetricsGraphiteConfiguration {

        @Autowired
        MetricRegistry metricRegistry;

        @PostConstruct
        public void startGraphiteReporter()
            throws UnknownHostException {
            var host = InetAddress.getLocalHost().getHostAddress();
            Graphite graphite = new Graphite("192.168.0.113", 2003);
            var reporter = GraphiteReporter.forRegistry(metricRegistry)
                                           .prefixedWith(String.format("services.movie %s", host))
                                           .build(graphite);
            reporter.start(30, TimeUnit.SECONDS);
        }

        @PostConstruct
        public void registerJVMMetrics() {
            metricRegistry.registerAll("gc",new GarbageCollectorMetricSet());
            metricRegistry.registerAll("memory",new MemoryUsageGaugeSet());
        }


    }

    @Autowired private ServerProperties serverProperties;
}
