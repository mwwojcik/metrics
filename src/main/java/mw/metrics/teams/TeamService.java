package mw.metrics.teams;

import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
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

    @Value("${user.service.host}")
    String userServiceHost;
    private WebClient webClient;
    private String detailsQueryString = "http://localhost:8080/team/{code}/details";
    private String playersQueryString = "http://localhost:8080/team/{code}/players";
    private String presidentQueryString = "http://localhost:8080/team/{code}/teampresident?delay={delay}";
    UnixOperatingSystemMXBean b;
    private long openFileDesc;
    private long maxFileDesc;
    private Random rand = new Random();
    public static List<Object> db = new ArrayList<>(100000);
    private RestTemplate restTemplate;

    public TeamService(WebClient webClient, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        b = (UnixOperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.webClient = webClient;
    }

    @Async("DetailsExecutor")
    public CompletableFuture<TeamScoreDTO> score(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(detailsQueryString).build(teamCode.name());
        makeHeapMesh("Score");
        var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamDetailsDTO.class).block();
        log.info(String.format("Open files %s, Max files %s", b.getOpenFileDescriptorCount(), b.getMaxFileDescriptorCount()));

        return CompletableFuture.completedFuture(TeamScoreDTO.from(teamCode, result.getPosition()));
    }

    @Async("PlayersExecutor")
    public CompletableFuture<TeamCaptainDTO> captain(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(playersQueryString).build(teamCode.name());
        //var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamPlayersDTO.class).block();

        var result = restTemplate.getForObject(uri, TeamPlayersDTO.class);

        log.info(String.format("Open files %s, Max files %s", b.getOpenFileDescriptorCount(), b.getMaxFileDescriptorCount()));
        makeHeapMesh("Captain");
        return CompletableFuture.completedFuture(TeamCaptainDTO.from(teamCode, result.getCaptain()));
    }

    @Async("PresidentsExecutor")
    public TeamPresidentDTO president(TeamCode teamCode) {

        var uri = UriComponentsBuilder.fromUriString(presidentQueryString).build(teamCode.name(), "500");

        var result = restTemplate.getForObject(uri, TeamPresidentDTO.class);

        return result;
    }

    private void makeHeapMesh(String marker) {
        db.add(new MyObject(marker, rand.nextInt()));
        db.add(rand.nextInt());
    }

    // PresidentsExecutor

    @AllArgsConstructor
    class MyObject {

        String name;
        Integer account;
    }
}
