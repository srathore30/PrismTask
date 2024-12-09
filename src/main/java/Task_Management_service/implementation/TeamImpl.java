package Task_Management_service.implementation;

import Task_Management_service.AuthUtils.JwtHelper;
import Task_Management_service.config.AuthConfig;
import Task_Management_service.constant.ApiErrorCodes;
import Task_Management_service.constant.UserStatus;
import Task_Management_service.dto.request.JwtRequest;
import Task_Management_service.dto.request.TeamMembersReq;
import Task_Management_service.dto.request.TeamReq;
import Task_Management_service.dto.request.UserReqDto;
import Task_Management_service.dto.response.JwtResponse;
import Task_Management_service.dto.response.TeamMembersRes;
import Task_Management_service.dto.response.TeamRes;
import Task_Management_service.dto.response.UserResDto;
import Task_Management_service.entity.TeamEntity;
import Task_Management_service.entity.TeamMembers;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.exception.ValidationException;
import Task_Management_service.repository.TeamMembersRepo;
import Task_Management_service.repository.TeamRepo;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.service.TeamServices;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamImpl implements TeamServices {
    private final TeamRepo teamRepo;

    public TeamImpl(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }


    @Override
    public TeamRes createTeam(TeamReq teamReq) {
        Optional<TeamEntity> optionalTeamEntity = teamRepo.findByTeamName(teamReq.getTeamName());
        if (optionalTeamEntity.isPresent()) {
            throw new ValidationException(
                    ApiErrorCodes.TEAM_ALREADY_EXIST.getErrorCode(),ApiErrorCodes.TEAM_ALREADY_EXIST.getErrorMessage()
            );
        }
        TeamEntity teamEntity = mapToEntity(teamReq);
        TeamEntity savedTeam = teamRepo.save(teamEntity);
        return mapToDto(savedTeam);

    }

    @Override
    public TeamRes getTeamById(Long id) {
        Optional<TeamEntity> optionalTeamEntity = teamRepo.findById(id);
        if (optionalTeamEntity.isEmpty()) {
            throw new NoSuchElementFoundException(
                    ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()
            );
        }

        return mapToDto(optionalTeamEntity.get());
    }


    @Override
    public List<TeamRes> getAllTeams() {
        List<TeamEntity> teamEntityList = teamRepo.findAll();
        return teamEntityList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTeamById(Long id) {
        Optional<TeamEntity> optionalTeamEntity = teamRepo.findById(id);
        if(optionalTeamEntity.isEmpty()){
            throw new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage());
        }
        teamRepo.delete(optionalTeamEntity.get());
    }


    private TeamRes mapToDto(TeamEntity teamEntity) {
        TeamRes teamRes = new TeamRes();
        teamRes.setId(teamEntity.getId());
        teamRes.setTeamName(teamEntity.getTeamName());
        teamRes.setDescription(teamEntity.getDescription());

        return teamRes;
    }

    private TeamEntity mapToEntity(TeamReq teamReq) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setTeamName(teamReq.getTeamName());
        teamEntity.setDescription(teamReq.getDescription());
        return teamEntity;
    }
}
