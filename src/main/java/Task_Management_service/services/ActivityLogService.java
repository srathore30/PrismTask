package Task_Management_service.services;

import Task_Management_service.dto.request.ActivityLogsReq;
import Task_Management_service.dto.request.CommentReq;
import Task_Management_service.dto.response.ActivityLogsRes;
import Task_Management_service.dto.response.CommentRes;

import java.util.List;

public interface ActivityLogService {
    ActivityLogsRes createActivityLogs(ActivityLogsReq activityLogsReq);
    ActivityLogsRes getActivityLogsById(Long id);
    List<ActivityLogsRes> getAllActivityLogs();
    void deleteActivityLogsById(Long id);
}
