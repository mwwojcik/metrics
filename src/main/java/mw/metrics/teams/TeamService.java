package mw.metrics.teams;

import java.net.URI;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

public class TeamService {

    private WebClient webClient;

    private String detailsQueryString="/team/{code}/details";
    private String playersQueryString="/team/{code}/players";

    public TeamService(WebClient webClient) {

        this.webClient = webClient;
    }

    public TeamScoreDTO score(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(detailsQueryString).build(teamCode.name());
        var result = webClient.get().uri(uri).retrieve();
        return null;
    }

    public TeamCaptainDTO captain(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(detailsQueryString).build(teamCode.name());
        var result = webClient.get().uri(uri).retrieve();
        return null;
    }
}
