package mw.metrics.teams.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamScoreDTO {

    private TeamCode code;
    private Integer score;

    public static TeamScoreDTO from(TeamCode code,Integer score) {
        return new TeamScoreDTO(code, score);
    }


}
