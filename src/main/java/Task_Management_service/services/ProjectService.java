package Task_Management_service.services;

import Task_Management_service.dto.request.ProjectRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.ProjectResponse;
import Task_Management_service.entity.ProjectEntity;
import Task_Management_service.repository.ProjectRepository;
import Task_Management_service.repository.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepo userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepo userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(ProjectRequest request) {
        if (!userRepository.existsById(request.getOwnerId())) {
            throw new RuntimeException("Owner not found with ID: " + request.getOwnerId());
        }

        ProjectEntity entity = mapDtoToEntity(request);
        return mapEntityToDto(projectRepository.save(entity));
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        if (!userRepository.existsById(request.getOwnerId())) {
            throw new RuntimeException("Owner not found with ID: " + request.getOwnerId());
        }

        Optional<ProjectEntity> entityOptional = projectRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + id);
        }

        ProjectEntity entity = entityOptional.get();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setOwnerId(request.getOwnerId());
        return mapEntityToDto(projectRepository.save(entity));
    }

    public ProjectResponse getProjectById(Long id) {
        Optional<ProjectEntity> entityOptional = projectRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        return mapEntityToDto(entityOptional.get());
    }

    public void deleteProject(Long id) {
        Optional<ProjectEntity> entityOptional = projectRepository.findById(id);
        if (entityOptional.isEmpty()) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.delete(entityOptional.get());
    }

    public PaginatedResp<ProjectResponse> getAllProjects(int page, int size, String sortBy, String sortDirection) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);

        var paginatedResult = projectRepository.findAll(pageRequest);
        List<ProjectResponse> projectResponses = paginatedResult.getContent().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(paginatedResult.getTotalElements(), paginatedResult.getTotalPages(), page, projectResponses);
    }


    private ProjectEntity mapDtoToEntity(ProjectRequest request) {
        ProjectEntity entity = new ProjectEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setOwnerId(request.getOwnerId());
        return entity;
    }

    private ProjectResponse mapEntityToDto(ProjectEntity entity) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setOwnerId(entity.getOwnerId());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
