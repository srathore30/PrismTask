package Task_Management_service.services;

import Task_Management_service.dto.request.WorkflowRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.WorkflowResponse;
import Task_Management_service.entity.ProjectEntity;
import Task_Management_service.entity.WorkflowEntity;
import Task_Management_service.repository.ProjectRepository;
import Task_Management_service.repository.WorkflowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final ProjectRepository projectRepository;

    public WorkflowService(WorkflowRepository workflowRepository, ProjectRepository projectRepository) {
        this.workflowRepository = workflowRepository;
        this.projectRepository = projectRepository;
    }

    public WorkflowResponse createWorkflow(WorkflowRequest request) {
        ProjectEntity projectEntity = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + request.getProjectId()));

        WorkflowEntity workflowEntity = new WorkflowEntity();
        workflowEntity.setName(request.getName());
        workflowEntity.setProject(projectEntity);

        WorkflowEntity savedEntity = workflowRepository.save(workflowEntity);
        return mapEntityToDto(savedEntity);
    }

    public WorkflowResponse getWorkflowById(Long workflowId) {
        WorkflowEntity workflowEntity = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found with ID: " + workflowId));
        return mapEntityToDto(workflowEntity);
    }

    public WorkflowResponse updateWorkflow(Long workflowId, WorkflowRequest request) {
        WorkflowEntity workflowEntity = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found with ID: " + workflowId));

        ProjectEntity projectEntity = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + request.getProjectId()));

        workflowEntity.setName(request.getName());
        workflowEntity.setProject(projectEntity);

        WorkflowEntity updatedEntity = workflowRepository.save(workflowEntity);
        return mapEntityToDto(updatedEntity);
    }

    public void deleteWorkflow(Long workflowId) {
        WorkflowEntity workflowEntity = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found with ID: " + workflowId));
        workflowRepository.delete(workflowEntity);
    }

    public PaginatedResp<WorkflowResponse> getAllWorkflows(int page, int size, String sortBy, String sortDirection) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page and size must be positive.");
        }

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WorkflowEntity> workflowPage = workflowRepository.findAll(pageable);

        List<WorkflowResponse> responses = workflowPage.getContent().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(workflowPage.getTotalElements(), workflowPage.getTotalPages(), page, responses);
    }

    private WorkflowResponse mapEntityToDto(WorkflowEntity workflowEntity) {
        WorkflowResponse response = new WorkflowResponse();
        response.setWorkflowId(workflowEntity.getId());
        response.setName(workflowEntity.getName());
        response.setProjectId(workflowEntity.getProject().getId());
        return response;
    }
}
