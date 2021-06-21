package mw.metrics.teams;

import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
import mw.metrics.teams.model.TeamPlayersDTO;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

public class TeamService {

    private WebClient webClient;
    private String detailsQueryString = "http://localhost:8080/team/{code}/details";
    private String playersQueryString = "http://localhost:8080/team/{code}/players";

    public TeamService(WebClient webClient) {

        this.webClient = webClient;
    }

    public TeamScoreDTO score(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(detailsQueryString).build(teamCode.name());

        var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamDetailsDTO.class).block();
        return TeamScoreDTO.from(teamCode, result.getPosition());
    }

    public TeamCaptainDTO captain(TeamCode teamCode) {
        var uri = UriComponentsBuilder.fromUriString(playersQueryString).build(teamCode.name());
        var result = webClient.get().uri(uri).retrieve().bodyToMono(TeamPlayersDTO.class).block();
        return TeamCaptainDTO.from(teamCode, result.getCaptain());
    }
}
