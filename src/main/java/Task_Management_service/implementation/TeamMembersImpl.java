package Task_Management_service.implementation;

import Task_Management_service.constant.ApiErrorCodes;
import Task_Management_service.dto.request.TeamMembersReq;
import Task_Management_service.dto.response.TeamMembersRes;
import Task_Management_service.entity.TeamEntity;
import Task_Management_service.entity.TeamMembers;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.exception.ValidationException;
import Task_Management_service.repository.TeamMembersRepo;
import Task_Management_service.repository.TeamRepo;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.service.TeamMembersServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamMembersImpl implements TeamMembersServices {
    private final TeamMembersRepo teamMembersRepo;
    private final UserRepo userRepo;
    private final TeamRepo teamRepo;

    public TeamMembersImpl(TeamMembersRepo teamMembersRepo, UserRepo userRepo, TeamRepo teamRepo) {
        this.teamMembersRepo = teamMembersRepo;
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
    }

    @Override
    public TeamMembersRes createTeamMember(TeamMembersReq teamMembersReq) {
        UserEntity user = userRepo.findById(teamMembersReq.getUserId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(),ApiErrorCodes.USER_NOT_FOUND.getErrorMessage() ));
        TeamEntity team = teamRepo.findById(teamMembersReq.getTeamId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(),ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));
        Optional<TeamMembers> existingMember = teamMembersRepo.findByTeamAndUser(team, user);
        if (existingMember.isPresent()) {
            throw new ValidationException(ApiErrorCodes.USER_ALREADY_EXIST.getErrorCode(),ApiErrorCodes.USER_ALREADY_EXIST.getErrorMessage());
        }
        TeamMembers teamMember = mapToEntity(teamMembersReq, team, user);
        TeamMembers savedTeamMember = teamMembersRepo.save(teamMember);
        return mapToDto(savedTeamMember);
    }

    @Override
    public void deleteTeamMemberById(Long id) {
        TeamMembers teamMember = teamMembersRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_MEMBER_NOT_FOUND.getErrorCode(),ApiErrorCodes.TEAM_MEMBER_NOT_FOUND.getErrorMessage()));
        teamMembersRepo.delete(teamMember);
    }

    @Override
    public TeamMembersRes getTeamMemberById(Long id) {
        TeamMembers teamMember = teamMembersRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_MEMBER_NOT_FOUND.getErrorCode(),ApiErrorCodes.TEAM_MEMBER_NOT_FOUND.getErrorMessage()));

        return mapToDto(teamMember);
    }

    @Override
    public List<TeamMembersRes> getAllTeamMembers() {
        List<TeamMembers> teamMembersList = teamMembersRepo.findAll();
        return teamMembersList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TeamMembersRes mapToDto(TeamMembers teamMember) {
        TeamMembersRes teamMembersRes = new TeamMembersRes();
        teamMembersRes.setId(teamMember.getId());
        teamMembersRes.setTeamId(teamMember.getTeam().getId());
        teamMembersRes.setTeamName(teamMember.getTeam().getTeamName());
        teamMembersRes.setUserId(teamMember.getUser().getId());
        teamMembersRes.setUserName(teamMember.getUser().getUsername());
        teamMembersRes.setRole(teamMember.getRole());
        return teamMembersRes;
    }

    private TeamMembers mapToEntity(TeamMembersReq teamMembersReq, TeamEntity team, UserEntity user) {
        TeamMembers teamMember = new TeamMembers();
        teamMember.setTeam(team);
        teamMember.setUser(user);
        teamMember.setRole(teamMembersReq.getRole());
        return teamMember;
    }
}
