package Task_Management_service.controller;

import Task_Management_service.dto.request.TeamMembersReq;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.TeamMembersRes;
import Task_Management_service.dto.response.UserResDto;
import Task_Management_service.service.TeamMembersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teamMember")
public class TeamMemberController {
    @Autowired
    private final TeamMembersServices teamMembersServices;

    public TeamMemberController(TeamMembersServices teamMembersServices) {
        this.teamMembersServices = teamMembersServices;
    }

    @PostMapping("/create")
    public ResponseEntity<TeamMembersRes> createTeamMember(@RequestBody TeamMembersReq teamMembersReq) {
        TeamMembersRes reqs = teamMembersServices.createTeamMember(teamMembersReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(reqs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamMembersRes> getTeamMemberById(@PathVariable Long id ){
        TeamMembersRes resp = teamMembersServices.getTeamMemberById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(resp);
    }
//    @PutMapping("/{flatId}")
//
//    public ResponseEntity<String> updateFlatDetails(@Validated @RequestBody FlatUpdateReq flatUpdateReq, @PathVariable Long flatId){
//        flatUpdateReq.setFlatOwnerId(flatId);
//        flatOwnerService.updateFlatDetails(flatUpdateReq);
//        return  ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
//    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamMembersRes>> getAllTeamMembers() {
        List<TeamMembersRes> teamMembersResList = teamMembersServices.getAllTeamMembers();
        return new ResponseEntity<>(teamMembersResList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamMemberById(@PathVariable Long id) {
        teamMembersServices.deleteTeamMemberById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
