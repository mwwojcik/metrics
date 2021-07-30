package mw.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.FastRespondingTeamPlayersService;
import mw.metrics.teams.SlowRespondingTeamDetailService;
import mw.metrics.teams.TeamService;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
@Slf4j
@EnableAsync
public class MetricsConfiguration {

    @PostConstruct
    public void init() {
        for (int i = 0; i < 1000; i++) {
            (new MyThread()).start();
            log.info("Thread created=>" + i);
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "MWO-METRICS-SPRING-BOOT-APP");
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public WebClient webClient() {
/*        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().responseTimeout(
            Duration.ofSeconds(1)).compress(true))).build();*/
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector()).build();
    }

    @Bean
    public TeamService teamService(SlowRespondingTeamDetailService detailService,
                                   FastRespondingTeamPlayersService fastRespondingTeamPlayersService,
                                   MeterRegistry meterRegistry) {
        return new TeamService(fastRespondingTeamPlayersService, detailService,meterRegistry);
    }

    @Bean
    public SlowRespondingTeamDetailService slowRespondingTeamDetailService() {
        return new SlowRespondingTeamDetailService();
    }

    @Bean
    public FastRespondingTeamPlayersService fastRespTeamPlayerService() {
        return new FastRespondingTeamPlayersService();
    }

}

class MyThread extends Thread {

    // List<MyObject> list = new ArrayList<>(600);

    @Override
    public void run() {
        Sleeper.sleepSecconds(120);
        mwstart();
        Sleeper.sleepSecconds(120);
        mwstart();
    }

    private void mwstart() {
        for (int i = 0; i < 1000; i++) {
            var item = new MyObject(Thread.currentThread().getName(), i);
            TeamService.db.add(item);
            NotSoGoodStorage.db.add(item);
            System.out.println("New Object added! Thread=>" + Thread.currentThread().getName());
        }
    }
}

@AllArgsConstructor
class MyObject {

    String name;
    Integer account;
}

class NotSoGoodStorage {

    public static List<Object> db = new ArrayList<>();
}

