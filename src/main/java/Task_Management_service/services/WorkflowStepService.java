package Task_Management_service.services;

import Task_Management_service.dto.request.WorkflowStepRequest;
import Task_Management_service.dto.response.WorkflowStepResponse;
import Task_Management_service.entity.WorkflowEntity;
import Task_Management_service.entity.WorkflowStepEntity;
import Task_Management_service.repository.WorkflowRepository;
import Task_Management_service.repository.WorkflowStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class WorkflowStepService {

    private final WorkflowStepRepository stepRepository;
    private final WorkflowRepository workflowRepository;

    public WorkflowStepService(WorkflowStepRepository stepRepository, WorkflowRepository workflowRepository) {
        this.stepRepository = stepRepository;
        this.workflowRepository = workflowRepository;
    }

    public WorkflowStepResponse createStep(WorkflowStepRequest request) {
        WorkflowEntity workflow = workflowRepository.findById(request.getWorkflowId())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found with ID: " + request.getWorkflowId()));

        WorkflowStepEntity step = new WorkflowStepEntity();
        step.setName(request.getName());
        step.setPosition(request.getPosition());
        step.setWorkflow(workflow);

        WorkflowStepEntity savedStep = stepRepository.save(step);
        return mapEntityToDto(savedStep);
    }

    public List<WorkflowStepResponse> getStepsByWorkflowId(Long workflowId) {
        List<WorkflowStepEntity> steps = stepRepository.findByWorkflowIdOrderByPositionAsc(workflowId);
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("No steps found for Workflow ID: " + workflowId);
        }
        return steps.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public WorkflowStepResponse updateStep(Long stepId, WorkflowStepRequest request) {
        WorkflowStepEntity step = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found with ID: " + stepId));

        WorkflowEntity workflow = workflowRepository.findById(request.getWorkflowId())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found with ID: " + request.getWorkflowId()));

        step.setName(request.getName());
        step.setPosition(request.getPosition());
        step.setWorkflow(workflow);

        WorkflowStepEntity updatedStep = stepRepository.save(step);
        return mapEntityToDto(updatedStep);
    }

    public void deleteStep(Long stepId) {
        WorkflowStepEntity step = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found with ID: " + stepId));
        stepRepository.delete(step);
    }

    private WorkflowStepResponse mapEntityToDto(WorkflowStepEntity step) {
        WorkflowStepResponse response = new WorkflowStepResponse();
        response.setStepId(step.getId());
        response.setName(step.getName());
        response.setPosition(step.getPosition());
        response.setWorkflowId(step.getWorkflow().getId());
        return response;
    }
}
