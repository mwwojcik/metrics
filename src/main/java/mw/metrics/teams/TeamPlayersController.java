package mw.metrics.teams;

import java.util.Map;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamPlayersDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamPlayersController {

   private FastRespondingTeamPlayersService fastRespondingTeamPlayersService;

    public TeamPlayersController(FastRespondingTeamPlayersService fastRespondingTeamPlayersService) {
        this.fastRespondingTeamPlayersService = fastRespondingTeamPlayersService;
    }

    @GetMapping("/{code}/players")
    public ResponseEntity<TeamPlayersDTO> getPlayers(@PathVariable(name = "code") String teamCode) {
        var key = TeamCode.valueOf(teamCode);
        var res = fastRespondingTeamPlayersService.get(key);
        return ResponseEntity.ok(res);
    }

}

