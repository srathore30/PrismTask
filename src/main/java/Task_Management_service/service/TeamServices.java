package Task_Management_service.service;

import Task_Management_service.dto.request.JwtRequest;
import Task_Management_service.dto.request.TeamReq;
import Task_Management_service.dto.request.UserReqDto;
import Task_Management_service.dto.response.JwtResponse;
import Task_Management_service.dto.response.TeamRes;
import Task_Management_service.dto.response.UserResDto;

import java.util.List;

public interface TeamServices {
    TeamRes createTeam(TeamReq teamReq);
    TeamRes getTeamById(Long id);
    List<TeamRes> getAllTeams();
    void deleteTeamById(Long id);
}
