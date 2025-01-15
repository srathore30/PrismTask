package Task_Management_service.controller;

import Task_Management_service.dto.request.ReportRequest;
import Task_Management_service.dto.request.ReportUpdateRequest;
import Task_Management_service.dto.request.ReportBulkRequest;
import Task_Management_service.dto.request.ReportBulkUpdateRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.ReportResponse;
import Task_Management_service.implementation.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReportResponse> createReport(@RequestBody ReportRequest request) {
        ReportResponse response = reportService.createReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        ReportResponse response = reportService.getReportById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable Long id, @RequestBody ReportUpdateRequest request) {
        ReportResponse response = reportService.updateReport(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<PaginatedResp<ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        PaginatedResp<ReportResponse> response = reportService.getAllReports(page, size, sortBy, sortDirection);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ReportResponse>> createReportsInBulk(@RequestBody ReportBulkRequest reportBulkRequest) {
        List<ReportResponse> response = reportService.createReportsInBulk(reportBulkRequest.getReports());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/list")
    public ResponseEntity<List<ReportResponse>> updateReportsInBulk(@RequestBody ReportBulkUpdateRequest reportBulkUpdateRequest) {
        List<ReportResponse> response = reportService.updateReportsInBulk(reportBulkUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
