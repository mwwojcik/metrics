package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamPresidentDTO {

    private TeamCode code;
    private String president;

    public static TeamPresidentDTO from(TeamCode code, String president) {
        return new TeamPresidentDTO(code, president);
    }


}
