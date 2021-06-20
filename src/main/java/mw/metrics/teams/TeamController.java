package mw.metrics.teams;

import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @GetMapping("/{code}/score")
    public ResponseEntity<TeamScoreDTO> score(@PathVariable(name = "code") String teamCode) {
        var res = service.score(TeamCode.valueOf(teamCode));
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{code}/captain")
    public ResponseEntity<TeamCaptainDTO> captain(@PathVariable(name = "code") String teamCode) {
        var res = service.captain(TeamCode.valueOf(teamCode));
        return ResponseEntity.ok(res);
    }
}
