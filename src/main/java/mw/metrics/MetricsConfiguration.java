package mw.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Configuration
    public static class MetricsGraphiteConfiguration{
        @Autowired
        MetricRegistry metricRegistry;

        @PostConstruct
        public void startGraphiteReporter() throws UnknownHostException {

            Graphite graphite=new Graphite("192.168.0.113",2003);
            var reporter = GraphiteReporter.forRegistry(metricRegistry).prefixedWith("services.movie").build(graphite);
            reporter.start(10, TimeUnit.SECONDS);
        }

    }
}
