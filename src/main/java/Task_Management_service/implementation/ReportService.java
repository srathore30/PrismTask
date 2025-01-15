package Task_Management_service.implementation;

import Task_Management_service.dto.request.ReportBulkUpdateRequest;
import Task_Management_service.dto.request.ReportRequest;
import Task_Management_service.dto.request.ReportUpdateRequest;
import Task_Management_service.dto.response.PaginatedResp;
import Task_Management_service.dto.response.ReportResponse;
import Task_Management_service.entity.ReportEntity;
import Task_Management_service.entity.TeamEntity;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.repository.ReportRepository;
import Task_Management_service.repository.TeamRepo;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.constant.ApiErrorCodes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepo userRepository;
    private final TeamRepo teamRepo;

    public ReportService(ReportRepository reportRepository, UserRepo userRepository, TeamRepo teamRepo) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.teamRepo = teamRepo;
    }

    public ReportResponse createReport(ReportRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()));

        TeamEntity team = teamRepo.findById(request.getTeamId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

        ReportEntity entity = mapDtoToEntity(request, user, team);
        return mapEntityToDto(reportRepository.save(entity));
    }

    public ReportResponse getReportById(Long id) {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.REPORT_NOT_FOUND.getErrorCode(), ApiErrorCodes.REPORT_NOT_FOUND.getErrorMessage()));
        return mapEntityToDto(report);
    }

    public ReportResponse updateReport(Long id, ReportUpdateRequest request) {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.REPORT_NOT_FOUND.getErrorCode(), ApiErrorCodes.REPORT_NOT_FOUND.getErrorMessage()));

        TeamEntity team = teamRepo.findById(request.getTeamId())
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

        report.setTitle(request.getTitle());
        report.setDescription(request.getDescription());
        report.setTeam(team);

        return mapEntityToDto(reportRepository.save(report));
    }

    public void deleteReport(Long id) {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.REPORT_NOT_FOUND.getErrorCode(), ApiErrorCodes.REPORT_NOT_FOUND.getErrorMessage()));
        reportRepository.delete(report);
    }

    public PaginatedResp<ReportResponse> getAllReports(int page, int size, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);

        var paginatedResult = reportRepository.findAll(pageRequest);
        List<ReportResponse> reportResponses = paginatedResult.getContent().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return new PaginatedResp<>(paginatedResult.getTotalElements(), paginatedResult.getTotalPages(), page, reportResponses);
    }

    public List<ReportResponse> createReportsInBulk(List<ReportRequest> reportRequests) {
        List<ReportEntity> entities = reportRequests.stream().map(request -> {
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()));

            TeamEntity team = teamRepo.findById(request.getTeamId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

            return mapDtoToEntity(request, user, team);
        }).collect(Collectors.toList());

        List<ReportEntity> savedEntities = reportRepository.saveAll(entities);
        return savedEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public List<ReportResponse> updateReportsInBulk(ReportBulkUpdateRequest request) {
        List<ReportEntity> reportsToUpdate = request.getReports().stream().map(updateRequest -> {
            ReportEntity existingReport = reportRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.REPORT_NOT_FOUND.getErrorCode(), ApiErrorCodes.REPORT_NOT_FOUND.getErrorMessage()));

            TeamEntity team = teamRepo.findById(updateRequest.getTeamId())
                    .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.TEAM_NOT_FOUND.getErrorCode(), ApiErrorCodes.TEAM_NOT_FOUND.getErrorMessage()));

            existingReport.setTitle(updateRequest.getTitle());
            existingReport.setDescription(updateRequest.getDescription());
            existingReport.setTeam(team);

            return existingReport;
        }).collect(Collectors.toList());
        List<ReportEntity> updatedReports = reportRepository.saveAll(reportsToUpdate);

        return updatedReports.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }
    private ReportEntity mapDtoToEntity(ReportRequest request, UserEntity user, TeamEntity team) {
        ReportEntity entity = new ReportEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setUser(user);
        entity.setTeam(team);
        return entity;
    }

    private ReportResponse mapEntityToDto(ReportEntity entity) {
        ReportResponse response = new ReportResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setUserId(entity.getUser().getId());
        response.setTeamId(entity.getTeam().getId());
        return response;
    }
}
