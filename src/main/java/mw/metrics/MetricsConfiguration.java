package mw.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MetricsConfiguration {



    @Configuration
    public static class MetricsGraphiteConfiguration {

        @Autowired
        private ServerProperties serverProperties;

       /* @PostConstruct
        public void startGraphiteReporter()
            throws UnknownHostException {
            var host = InetAddress.getLocalHost().getHostAddress();
            Graphite graphite = new Graphite("192.168.0.113", 2003);
            var reporter = GraphiteReporter.forRegistry(metricRegistry)
                                           .prefixedWith(String.format("services.movie %s", host))
                                           .build(graphite);
            reporter.start(30, TimeUnit.SECONDS);
        }*/

        @PostConstruct
        public void registerJVMMetrics(MeterRegistry meterRegistry) {

            /*metricRegistry.registerAll("memory",new MemoryUsageGaugeSet());
            metricRegistry.registerAll("gc",new GarbageCollectorMetricSet());*/
        }


    }
}
