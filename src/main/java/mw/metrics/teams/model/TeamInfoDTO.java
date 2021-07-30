package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamInfoDTO {

    private TeamCode code;
    private String captain;
    private String country;
    private Integer position;

    public static TeamInfoDTO from(TeamCode code, String captain, String country, Integer position) {
        return new TeamInfoDTO(code, captain, country, position);
    }


}
