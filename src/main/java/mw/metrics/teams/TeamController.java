package mw.metrics.teams;

import io.netty.util.concurrent.CompleteFuture;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import mw.metrics.teams.model.TeamCaptainDTO;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamPresidentDTO;
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
    public CompletableFuture<TeamScoreDTO> score(@PathVariable(name = "code") String teamCode)
        throws InterruptedException, ExecutionException {
        var res = service.score(TeamCode.valueOf(teamCode));
        log.info("GET on /team/score received!");
        var teamScoreDTO = res.get();
        return res;
    }

    @GetMapping("/{code}/captain")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<TeamCaptainDTO> captain(@PathVariable(name = "code") String teamCode)
        throws ExecutionException, InterruptedException {
        var res = service.captain(TeamCode.valueOf(teamCode));
        log.info("GET on /team/captain received!");
        var teamCaptainDTO = res.get();
        return res;

    }


}
