package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamPlayersDTO {

    private TeamCode code;
    private String captain;

    public static TeamPlayersDTO from(TeamCode code, String captain) {
        return new TeamPlayersDTO(code, captain);
    }


}
