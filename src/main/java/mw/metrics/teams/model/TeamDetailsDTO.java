package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamDetailsDTO {

    private TeamCode code;
    private String country;
    private Integer position;

    public static TeamDetailsDTO from(TeamCode code,  String country, Integer position) {
        return new TeamDetailsDTO(code,  country, position);
    }


}
