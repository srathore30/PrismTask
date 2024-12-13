package Task_Management_service.Controller;

import Task_Management_service.dto.request.TeamReq;
import Task_Management_service.dto.response.TeamRes;
import Task_Management_service.services.TeamServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private final TeamServices teamServices;


    public TeamController(TeamServices teamServices) {
        this.teamServices = teamServices;
    }

    @PostMapping("/create")
    public ResponseEntity<TeamRes> createTeam(@RequestBody TeamReq teamReq) {
        TeamRes reqs = teamServices.createTeam(teamReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(reqs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamRes> getTeamById(@PathVariable Long id ){
        TeamRes resp = teamServices.getTeamById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(resp);
    }
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamRes> updateTeam(@Validated @RequestBody TeamReq teamReq,@PathVariable Long teamId) {
        TeamRes updatedTeam = teamServices.updateTeam(teamId, teamReq);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamRes>> getAllTeams() {
        List<TeamRes> teamResList = teamServices.getAllTeams();
        return new ResponseEntity<>(teamResList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long id) {
        teamServices.deleteTeamById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
