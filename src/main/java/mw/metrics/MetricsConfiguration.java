package mw.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import mw.metrics.teams.FastRespondingTeamPlayersService;
import mw.metrics.teams.SlowRespondingTeamDetailService;
import mw.metrics.teams.TeamDetailsController;
import mw.metrics.teams.TeamPlayersController;
import mw.metrics.teams.TeamService;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableScheduling
public class MetricsConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "MWO-METRICS-SPRING-BOOT-APP");
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true))).build();
    }

    @Bean
    public TeamService teamService(WebClient webClient) {
        return new TeamService(webClient);
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
