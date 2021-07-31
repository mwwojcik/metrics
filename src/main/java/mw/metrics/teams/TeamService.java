package mw.metrics.teams;

import com.sun.management.UnixOperatingSystemMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
import mw.metrics.teams.model.TeamInfoDTO;
import mw.metrics.teams.model.TeamPlayersDTO;
import mw.metrics.teams.model.TeamPresidentDTO;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class TeamService {

    private Random rand = new Random();
    public static List<Object> db = new ArrayList<>(100000);
    private ExecutorService detailsServicePool = Executors.newFixedThreadPool(20);
    private ExecutorService playersServicePool = Executors.newFixedThreadPool(20);

    private FastRespondingTeamPlayersService teamPlayersService;
    private SlowRespondingTeamDetailService teamDetailService;


    public TeamService(FastRespondingTeamPlayersService teamPlayersService,
                       SlowRespondingTeamDetailService teamDetailService,
                       MeterRegistry meterRegistry) {
        this.teamPlayersService = teamPlayersService;
        this.teamDetailService = teamDetailService;

        new ExecutorServiceMetrics(detailsServicePool, "detailsServicePool", Tags.of("pool", "pool")).bindTo(meterRegistry);
        new ExecutorServiceMetrics(playersServicePool, "playersServicePool", Tags.of("pool", "pool")).bindTo(meterRegistry);
    }

    public CompletableFuture<TeamScoreDTO> score(TeamCode teamCode) {
        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamDetails(teamCode), detailsServicePool);
        return resultAsync.thenApply(it -> TeamScoreDTO.from(teamCode, it.getPosition()));
    }

    public CompletableFuture<TeamCaptainDTO> captain(TeamCode teamCode) {
        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamPlayer(teamCode), playersServicePool);
        return resultAsync.thenApply(it -> TeamCaptainDTO.from(teamCode, it.getCaptain()));
    }

    public CompletableFuture<TeamInfoDTO> details(TeamCode teamCode) {
        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamDetails(teamCode), detailsServicePool);
        var playersAsync = CompletableFuture.supplyAsync(() -> loadTeamPlayer(teamCode), playersServicePool);

        return resultAsync.thenCombine(playersAsync,
                                       (details, players) -> TeamInfoDTO.from(details.getCode(),
                                                                              players.getCaptain(),
                                                                              details.getCountry(),
                                                                              details.getPosition()));
    }

    private TeamPlayersDTO loadTeamPlayer(TeamCode teamCode) {
        makeHeapMesh("Captain");
        return teamPlayersService.get(teamCode);
    }

    private TeamDetailsDTO loadTeamDetails(TeamCode teamCode) {
        makeHeapMesh("Score");
        return teamDetailService.get(teamCode);
    }

    private void makeHeapMesh(String marker) {
        db.add(new MyObject(marker, rand.nextInt()));
        db.add(rand.nextInt());
    }

    @AllArgsConstructor
    class MyObject {

        String name;
        Integer account;
    }
}
