package mw.metrics.teams;

import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
import mw.metrics.teams.model.TeamPlayersDTO;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
@Slf4j
public class TeamService {

    private WebClient webClient;
    private String detailsQueryString = "http://localhost:8080/team/{code}/details";
    private String playersQueryString = "http://localhost:8080/team/{code}/players";

    UnixOperatingSystemMXBean b;

    private long openFileDesc;
    private long maxFileDesc;


    public TeamService(WebClient webClient) {
        b= (UnixOperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        this.webClient = webClient;
    }

    public TeamScoreDTO score(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(detailsQueryString).build(teamCode.name());

        var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamDetailsDTO.class).block();
        log.info(String.format("Open files %s, Max files %s", b.getOpenFileDescriptorCount(),b.getMaxFileDescriptorCount()));
        return TeamScoreDTO.from(teamCode,result.getPosition());
    }

    public TeamCaptainDTO captain(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(playersQueryString).build(teamCode.name());
        var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamPlayersDTO.class).block();
        log.info(String.format("Open files %s, Max files %s", b.getOpenFileDescriptorCount(),b.getMaxFileDescriptorCount()));
        return TeamCaptainDTO.from(teamCode, result.getCaptain());
    }
}
