package mw.metrics.teams;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
import mw.metrics.teams.model.TeamInfoDTO;
import mw.metrics.teams.model.TeamPlayersDTO;
import mw.metrics.teams.model.TeamScoreDTO;

@Slf4j
public class TeamService {

    private Random rand = new Random();
    public static List<Object> db = new ArrayList<>(100000);
    private ExecutorService detailsServicePool = new ThreadPoolExecutor(100,
                                                                        100,
                                                                        0L,
                                                                        TimeUnit.MILLISECONDS,
                                                                        new LinkedBlockingQueue<Runnable>(),
                                                                        MyThreadFactory.create());
    private ExecutorService playersServicePool = Executors.newFixedThreadPool(100);
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

        var parentName = Thread.currentThread().getName();

        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamDetails(teamCode, parentName), detailsServicePool)
                                           .whenComplete(handleAsyncException());

        return resultAsync.whenComplete(handleAsyncException()).thenApply(it -> TeamScoreDTO.from(teamCode, it.getPosition()));
    }

    public CompletableFuture<TeamCaptainDTO> captain(TeamCode teamCode) {
        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamPlayer(teamCode), playersServicePool);
        return resultAsync.thenApply(it -> TeamCaptainDTO.from(teamCode, it.getCaptain()));
    }

    public CompletableFuture<TeamInfoDTO> details(TeamCode teamCode) {
        var resultAsync = CompletableFuture.supplyAsync(() -> loadTeamDetails(teamCode, Thread.currentThread().getName()),
                                                        detailsServicePool);
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

    private TeamDetailsDTO loadTeamDetails(TeamCode teamCode, String parentThreadName) {
        makeHeapMesh("Score");
        return teamDetailService.get(teamCode, parentThreadName);
    }

    private BiConsumer<TeamDetailsDTO, Throwable> handleAsyncException() {
        return (msg, ex) -> {
            if (Objects.nonNull(ex)) {
                log.info("!!!!!!!!!!! Exception occured =>" + ex.getMessage());
            }

        };
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

class MyThreadFactory implements ThreadFactory {

    private int ordinal = 1;

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "CustomThread-" + ordinal++);
    }

    public static MyThreadFactory create() {
        return new MyThreadFactory();
    }


}