package mw.metrics.teams;

import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamScoreDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    private TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @GetMapping("/{code}/score")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TeamScoreDTO> score(@PathVariable(name = "code") String teamCode) {
        var res = service.score(TeamCode.valueOf(teamCode));
        log.info("GET on /team/score received!");
        return ResponseEntity.ok(res);
    }


    @GetMapping("/{code}/captain")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TeamCaptainDTO> captain(@PathVariable(name = "code") String teamCode) {
        var res = service.captain(TeamCode.valueOf(teamCode));
        log.info("GET on /team/captain received!");
        return ResponseEntity.ok(res);

    }
}
