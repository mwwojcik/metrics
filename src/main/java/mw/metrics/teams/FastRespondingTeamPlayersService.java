package mw.metrics.teams;

import java.util.Map;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamPlayersDTO;

public class FastRespondingTeamPlayersService {

    private Map<TeamCode, TeamPlayersDTO> db = Map.of(TeamCode.PL,
                                                      TeamPlayersDTO.from(TeamCode.PL, "Robert Lewandowski"),
                                                      TeamCode.FR,
                                                      TeamPlayersDTO.from(TeamCode.FR, "Hugo Lloris"),
                                                      TeamCode.GER,
                                                      TeamPlayersDTO.from(TeamCode.GER, "Manuel Neuer"),
                                                      TeamCode.ESP,
                                                      TeamPlayersDTO.from(TeamCode.ESP, "Iker Casillas")

                                                     );

    public TeamPlayersDTO get(TeamCode key) {
        return db.get(key);
    }
}
