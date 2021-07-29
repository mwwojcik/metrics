package mw.metrics.teams;

import com.sun.management.UnixOperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
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

    private Random rand = new Random();
    public static List<Object> db = new ArrayList<>(100000);
    private FastRespondingTeamPlayersService teamPlayersService;
    private SlowRespondingTeamDetailService teamDetailService;

    public TeamService(FastRespondingTeamPlayersService teamPlayersService, SlowRespondingTeamDetailService teamDetailService) {
        this.teamPlayersService = teamPlayersService;
        this.teamDetailService = teamDetailService;
    }

    public TeamScoreDTO score(TeamCode teamCode) {
        db.add(new MyObject("Score", rand.nextInt()));
        db.add(rand.nextInt());
        var result = teamDetailService.get(teamCode);
        return TeamScoreDTO.from(teamCode, result.getPosition());
    }

    public TeamCaptainDTO captain(TeamCode teamCode) {
        var result = teamPlayersService.get(teamCode);
        db.add(new MyObject("Captain", rand.nextInt()));
        db.add(rand.nextInt());
        return TeamCaptainDTO.from(teamCode, result.getCaptain());
    }

    @AllArgsConstructor
    class MyObject {

        String name;
        Integer account;
    }
}
