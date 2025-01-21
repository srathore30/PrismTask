package Task_Management_service.implementation;

import Task_Management_service.dto.request.AssigneeBulkUpdateRequest;
import Task_Management_service.dto.request.AssigneeRequest;
import Task_Management_service.dto.request.AssigneeUpdateRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.AssigneeResponse;
import Task_Management_service.entity.AssigneeEntity;
import Task_Management_service.entity.TaskEntity;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.repository.AssigneeRepository;
import Task_Management_service.repository.TaskRepository;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.constant.ApiErrorCodes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;
    private final UserRepo userRepository;
    private final TaskRepository taskRepo;

    public AssigneeService(AssigneeRepository assigneeRepository, UserRepo userRepository, TaskRepository taskRepo) {
        this.assigneeRepository = assigneeRepository;
        this.userRepository = userRepository;
        this.taskRepo = taskRepo;
    }

    public AssigneeResponse createAssignee(AssigneeRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()));

        TaskEntity task = taskRepo.findById(request.getTaskId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TASK_NOT_FOUND.getErrorCode(), ApiErrorCodes.TASK_NOT_FOUND.getErrorMessage()));

        AssigneeEntity entity = mapDtoToEntity(request, user, task);
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

        TaskEntity task = taskRepo.findById(request.getTaskId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TASK_NOT_FOUND.getErrorCode(), ApiErrorCodes.TASK_NOT_FOUND.getErrorMessage()));

        assignee.setTitle(request.getTitle());
        assignee.setDescription(request.getDescription());
        assignee.setTask(task);

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
        List<AssigneeResponse> assigneeResponses = paginatedResult.getContent().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(paginatedResult.getTotalElements(), paginatedResult.getTotalPages(), page, assigneeResponses);
    }

    public List<AssigneeResponse> createAssigneesInBulk(List<AssigneeRequest> assigneeRequests) {
        List<AssigneeEntity> entities = assigneeRequests.stream().map(request -> {
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()));

            TaskEntity task = taskRepo.findById(request.getTaskId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TASK_NOT_FOUND.getErrorCode(), ApiErrorCodes.TASK_NOT_FOUND.getErrorMessage()));

            return mapDtoToEntity(request, user, task);
        }).collect(Collectors.toList());

        List<AssigneeEntity> savedEntities = assigneeRepository.saveAll(entities);
        return savedEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<AssigneeResponse> updateAssigneesInBulk(AssigneeBulkUpdateRequest request) {
        List<AssigneeEntity> assigneesToUpdate = request.getAssignees().stream().map(updateRequest -> {
            AssigneeEntity existingAssignee = assigneeRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorCode(), ApiErrorCodes.ASSIGNEE_NOT_FOUND.getErrorMessage()));

            TaskEntity task = taskRepo.findById(updateRequest.getTaskId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TASK_NOT_FOUND.getErrorCode(), ApiErrorCodes.TASK_NOT_FOUND.getErrorMessage()));

            existingAssignee.setTitle(updateRequest.getTitle());
            existingAssignee.setDescription(updateRequest.getDescription());
            existingAssignee.setTask(task);

            return existingAssignee;
        }).collect(Collectors.toList());
        List<AssigneeEntity> updatedAssignees = assigneeRepository.saveAll(assigneesToUpdate);

        return updatedAssignees.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    private AssigneeEntity mapDtoToEntity(AssigneeRequest request, UserEntity user, TaskEntity task) {
        AssigneeEntity entity = new AssigneeEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setUser(user);
        entity.setTask(task);
        return entity;
    }

    private AssigneeResponse mapEntityToDto(AssigneeEntity entity) {
        AssigneeResponse response = new AssigneeResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setTaskId(entity.getTask().getId());
        response.setTaskName(entity.getTask().getTitle());
        response.setUserId(entity.getUser().getId());
        response.setUserName(entity.getUser().getUsername());
        return response;
    }
}
