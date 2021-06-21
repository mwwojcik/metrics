package mw.metrics.teams;

import java.util.Map;
import mw.metrics.Sleeper;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;

public class SlowRespondingTeamDetailService {

    private Map<TeamCode, TeamDetailsDTO> db = Map.of(TeamCode.PL,
                                                      TeamDetailsDTO.from(TeamCode.PL, "Poland", 30),
                                                      TeamCode.FR,
                                                      TeamDetailsDTO.from(TeamCode.FR, "France", 5),
                                                      TeamCode.GER,
                                                      TeamDetailsDTO.from(TeamCode.GER, "Germany", 10),
                                                      TeamCode.ESP,
                                                      TeamDetailsDTO.from(TeamCode.PL, "Spain", 4));

    public TeamDetailsDTO get(TeamCode key) {
        return db.get(key);
    }
}
