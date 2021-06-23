package mw.metrics.teams;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.Sleeper;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamDetailsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
@Slf4j
public class TeamDetailsController {

    private SlowRespondingTeamDetailService slowRespService ;

    public TeamDetailsController(SlowRespondingTeamDetailService slowRespService) {
        this.slowRespService = slowRespService;
    }

    @GetMapping("/{code}/details")
    public ResponseEntity<TeamDetailsDTO> getDetails(@PathVariable(name = "code") String teamCode) {
        var key = TeamCode.valueOf(teamCode);
        var res = slowRespService.get(key);
        log.info("GET on /team/details received!");
        return ResponseEntity.ok(res);
    }

}

