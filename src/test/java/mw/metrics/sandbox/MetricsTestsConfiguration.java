package mw.metrics.sandbox;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MetricsTestsConfiguration {

   /* @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }*/

    @Configuration
    public static class MetricsGraphiteConfiguration {

       /* @Autowired
        MetricRegistry metricRegistry;*/

      /*  @PostConstruct
        public void startGraphiteReporter()
            throws UnknownHostException {
            var host = "WIN";
            Graphite graphite = new Graphite("localhost", 2003);
            var reporter = GraphiteReporter.forRegistry(metricRegistry)
                                           .prefixedWith(String.format("mw.hashmap %s", host))
                                           .build(graphite);
            reporter.start(3, TimeUnit.SECONDS);
        }*/

        @PostConstruct
        public void initGCMetrics() {
            /*metricRegistry.registerAll("GC", new GarbageCollectorMetricSet());
            metricRegistry.registerAll("MEMORY", new MemoryUsageGaugeSet());*/
        }
    }
}
