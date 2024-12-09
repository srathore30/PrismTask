package Task_Management_service.implementation;

import Task_Management_service.constant.ApiErrorCodes;
import Task_Management_service.dto.request.ActivityLogsReq;
import Task_Management_service.dto.response.ActivityLogsRes;
import Task_Management_service.entity.ActivityLogsEntity;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.repository.ActivityLogsRepo;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.service.ActivityLogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityLogsImpl implements ActivityLogService {
    private final ActivityLogsRepo activityLogsRepo;
    private final UserRepo userRepo;

    public ActivityLogsImpl(ActivityLogsRepo activityLogsRepo, UserRepo userRepo) {
        this.activityLogsRepo = activityLogsRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ActivityLogsRes createActivityLogs(ActivityLogsReq activityLogsReq) {
        UserEntity user = userRepo.findById(activityLogsReq.getUserId())
                .orElseThrow(() -> new NoSuchElementFoundException( ApiErrorCodes.USER_NOT_FOUND.getErrorCode(),ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()));
        ActivityLogsEntity activityLogsEntity = mapToEntity(activityLogsReq, user);
        ActivityLogsEntity savedActivityLogs = activityLogsRepo.save(activityLogsEntity);
        return mapToDto(savedActivityLogs);
    }

    @Override
    public ActivityLogsRes getActivityLogsById(Long id) {
        ActivityLogsEntity activityLogsEntity = activityLogsRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException( ApiErrorCodes.ACTIVITY_LOGS_NOT_FOUND.getErrorCode(),ApiErrorCodes.ACTIVITY_LOGS_NOT_FOUND.getErrorMessage()));

        return mapToDto(activityLogsEntity);
    }

    @Override
    public List<ActivityLogsRes> getAllActivityLogs() {
        List<ActivityLogsEntity> activityLogsEntityList = activityLogsRepo.findAll();
        return activityLogsEntityList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteActivityLogsById(Long id) {
        ActivityLogsEntity activityLogsEntity = activityLogsRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(ApiErrorCodes.ACTIVITY_LOGS_NOT_FOUND.getErrorCode(),ApiErrorCodes.ACTIVITY_LOGS_NOT_FOUND.getErrorMessage()));
        activityLogsRepo.delete(activityLogsEntity);
    }

    private ActivityLogsRes mapToDto(ActivityLogsEntity activityLogsEntity) {
        ActivityLogsRes activityLogsRes = new ActivityLogsRes();
        activityLogsRes.setId(activityLogsEntity.getId());
        activityLogsRes.setActivityType(activityLogsEntity.getActivityType());
        activityLogsRes.setUserId(activityLogsEntity.getUser().getId());
        activityLogsRes.setUserName(activityLogsEntity.getUser().getUsername());
        activityLogsRes.setAction(activityLogsEntity.getAction());
        activityLogsRes.setEntityId(activityLogsEntity.getEntityId());

        return activityLogsRes;
    }

    private ActivityLogsEntity mapToEntity(ActivityLogsReq activityLogsReq, UserEntity user) {
        ActivityLogsEntity activityLogsEntity = new ActivityLogsEntity();
        activityLogsEntity.setActivityType(activityLogsReq.getActivityType());
        activityLogsEntity.setAction(activityLogsReq.getAction());
        activityLogsEntity.setEntityId(activityLogsReq.getEntityId());
        activityLogsEntity.setUser(user);

        return activityLogsEntity;
    }
}
