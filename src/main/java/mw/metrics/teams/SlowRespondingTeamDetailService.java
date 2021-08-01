package mw.metrics.teams;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.Sleeper;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;

@Slf4j
public class SlowRespondingTeamDetailService {

    private Map<TeamCode, TeamDetailsDTO> db = Map.of(TeamCode.PL,
                                                      TeamDetailsDTO.from(TeamCode.PL, "Poland", 30),
                                                      TeamCode.FR,
                                                      TeamDetailsDTO.from(TeamCode.FR, "France", 5),
                                                      TeamCode.GER,
                                                      TeamDetailsDTO.from(TeamCode.GER, "Germany", 10),
                                                      TeamCode.ESP,
                                                      TeamDetailsDTO.from(TeamCode.PL, "Spain", 4));

    public TeamDetailsDTO get(TeamCode key,String parentThreadName) {

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            log.info("= Run with thread => "+Thread.currentThread().getName() + " parent-thread=> "+ parentThreadName);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        return TeamDetailsDTO.from(TeamCode.PL, "Poland", 30);
    }
}
