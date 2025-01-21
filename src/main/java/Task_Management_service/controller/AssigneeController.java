package Task_Management_service.controller;

import Task_Management_service.dto.request.AssigneeRequest;
import Task_Management_service.dto.request.AssigneeUpdateRequest;
import Task_Management_service.dto.request.AssigneeBulkUpdateRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.AssigneeResponse;
import Task_Management_service.implementation.AssigneeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignees")
public class AssigneeController {

    private final AssigneeService assigneeService;

    public AssigneeController(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    @PostMapping("/create")
    public ResponseEntity<AssigneeResponse> createAssignee(@RequestBody AssigneeRequest request) {
        AssigneeResponse response = assigneeService.createAssignee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssigneeResponse> getAssigneeById(@PathVariable Long id) {
        AssigneeResponse response = assigneeService.getAssigneeById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssigneeResponse> updateAssignee(@PathVariable Long id, @RequestBody AssigneeUpdateRequest request) {
        AssigneeResponse response = assigneeService.updateAssignee(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignee(@PathVariable Long id) {
        assigneeService.deleteAssignee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<PaginatedResp<AssigneeResponse>> getAllAssignees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<AssigneeResponse> response = assigneeService.getAllAssignees(page, size, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AssigneeResponse>> createAssigneesInBulk(@RequestBody List<AssigneeRequest> assigneeRequests) {
        List<AssigneeResponse> response = assigneeService.createAssigneesInBulk(assigneeRequests);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/list")
    public ResponseEntity<List<AssigneeResponse>> updateAssigneesInBulk(@RequestBody AssigneeBulkUpdateRequest assigneeBulkUpdateRequest) {
        List<AssigneeResponse> response = assigneeService.updateAssigneesInBulk(assigneeBulkUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
