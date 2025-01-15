package Task_Management_service.implementation;

import Task_Management_service.dto.request.AssigneeBulkUpdateRequest;
import Task_Management_service.dto.request.AssigneeRequest;
import Task_Management_service.dto.request.AssigneeUpdateRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.AssigneeResponse;
import Task_Management_service.entity.AssigneeEntity;
import Task_Management_service.entity.TeamEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.repository.AssigneeRepository;
import Task_Management_service.repository.TeamRepo;
import Task_Management_service.constant.ApiErrorCodes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;
    private final TeamRepo teamRepo;

    public AssigneeService(AssigneeRepository assigneeRepository, TeamRepo teamRepo) {
        this.assigneeRepository = assigneeRepository;
        this.teamRepo = teamRepo;
    }

    public AssigneeResponse createAssignee(AssigneeRequest request) {
        TeamEntity team = teamRepo.findById(request.getTeamId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

        AssigneeEntity entity = new AssigneeEntity();
        entity.setDescription(request.getDescription());
        entity.setTeam(team);

        return mapEntityToDto(assigneeRepository.save(entity));
    }

    public AssigneeResponse getAssigneeById(Long id) {
        AssigneeEntity assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorCode(), ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorMessage()));
        return mapEntityToDto(assignee);
    }

    public AssigneeResponse updateAssignee(Long id, AssigneeUpdateRequest request) {
        AssigneeEntity assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorCode(), ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorMessage()));

        TeamEntity team = teamRepo.findById(request.getTeamId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

        assignee.setDescription(request.getDescription());
        assignee.setTeam(team);

        return mapEntityToDto(assigneeRepository.save(assignee));
    }

    public void deleteAssignee(Long id) {
        AssigneeEntity assignee = assigneeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorCode(), ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorMessage()));
        assigneeRepository.delete(assignee);
    }

    public PaginatedResp<AssigneeResponse> getAllAssignees(int page, int size, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);

        var paginatedResult = assigneeRepository.findAll(pageRequest);
        List<AssigneeResponse> responses = paginatedResult.getContent().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(paginatedResult.getTotalElements(), paginatedResult.getTotalPages(), page, responses);
    }

    public List<AssigneeResponse> createAssigneesInBulk(List<AssigneeRequest> requests) {
        List<AssigneeEntity> entities = requests.stream().map(request -> {
            TeamEntity team = teamRepo.findById(request.getTeamId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

            AssigneeEntity entity = new AssigneeEntity();
            entity.setDescription(request.getDescription());
            entity.setTeam(team);

            return entity;
        }).collect(Collectors.toList());

        List<AssigneeEntity> savedEntities = assigneeRepository.saveAll(entities);
        return savedEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<AssigneeResponse> updateAssigneesInBulk(AssigneeBulkUpdateRequest request) {
        List<AssigneeEntity> entities = request.getAssignees().stream().map(updateRequest -> {
            AssigneeEntity existingEntity = assigneeRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorCode(), ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorMessage()));

            TeamEntity team = teamRepo.findById(updateRequest.getTeamId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

            existingEntity.setDescription(updateRequest.getDescription());
            existingEntity.setTeam(team);

            return existingEntity;
        }).collect(Collectors.toList());

        List<AssigneeEntity> updatedEntities = assigneeRepository.saveAll(entities);
        return updatedEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    private AssigneeResponse mapEntityToDto(AssigneeEntity entity) {
        AssigneeResponse response = new AssigneeResponse();
        response.setId(entity.getId());
        response.setDescription(entity.getDescription());
        response.setTeamId(entity.getTeam().getId());
        return response;
    }
}
