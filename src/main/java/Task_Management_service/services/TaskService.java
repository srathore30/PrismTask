package Task_Management_service.services;

import Task_Management_service.dto.request.TaskRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.TaskResponse;
import Task_Management_service.entity.TaskEntity;
import Task_Management_service.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        TaskEntity entity = mapDtoToEntity(request);
        TaskEntity savedEntity = taskRepository.save(entity);
        return mapEntityToDto(savedEntity);
    }

    public TaskResponse getTaskById(Long id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        return mapEntityToDto(entity);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus());
        entity.setPriority(request.getPriority());
        entity.setType(request.getType());
        entity.setDueDate(request.getDueDate());
        return mapEntityToDto(taskRepository.save(entity));
    }

    public void deleteTask(Long id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
        taskRepository.delete(entity);
    }

    public PaginatedResp<TaskResponse> getAllTasks(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TaskEntity> taskPage = taskRepository.findAll(pageable);

        List<TaskResponse> responseList = taskPage.getContent()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                page,
                responseList
        );
    }

    private TaskEntity mapDtoToEntity(TaskRequest request) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus());
        entity.setPriority(request.getPriority());
        entity.setType(request.getType());
        entity.setDueDate(request.getDueDate());
        return entity;
    }

    private TaskResponse mapEntityToDto(TaskEntity entity) {
        TaskResponse response = new TaskResponse();
        response.setTaskId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setStatus(entity.getStatus());
        response.setPriority(entity.getPriority());
        response.setType(entity.getType());
        response.setDueDate(entity.getDueDate());
        response.setCreatedAt(entity.getCreatedTime());
        return response;
    }

}
