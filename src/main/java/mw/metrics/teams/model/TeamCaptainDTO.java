package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TeamCaptainDTO {

    private TeamCode code;
    private String captain;

    public static TeamCaptainDTO from(TeamCode code, String captain) {
        return new TeamCaptainDTO(code, captain);
    }


}
