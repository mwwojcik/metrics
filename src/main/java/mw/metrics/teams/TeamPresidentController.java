package mw.metrics.teams;

import java.time.Duration;
import mw.metrics.teams.model.TeamCode;
import mw.metrics.teams.model.TeamPresidentDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/team")
public class TeamPresidentController {
    @GetMapping("/{teamCode}/teampresident")
    public Mono<TeamPresidentDTO> getPresidentWithDelay(@PathVariable String teamCode, @RequestParam long delay) {
        return Mono.just(TeamPresidentDTO.from(TeamCode.valueOf(teamCode), "TestPresident")).delayElement(Duration.ofMillis(delay));
    }
}
