package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamCaptainDTO {

    private TeamCode code;
    private String captain;

    public static TeamCaptainDTO from(TeamCode code, String captain) {
        return new TeamCaptainDTO(code, captain);
    }


}
