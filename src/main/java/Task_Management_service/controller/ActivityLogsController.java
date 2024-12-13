package Task_Management_service.Controller;

import Task_Management_service.dto.request.ActivityLogsReq;
import Task_Management_service.dto.response.ActivityLogsRes;
import Task_Management_service.services.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activityLogs")
public class ActivityLogsController {
    @Autowired
    private final ActivityLogService activityLogService;

    public ActivityLogsController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @PostMapping("/create")
    public ResponseEntity<ActivityLogsRes> createActivityLogs(@RequestBody ActivityLogsReq activityLogsReq) {
        ActivityLogsRes reqs = activityLogService.createActivityLogs(activityLogsReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(reqs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityLogsRes> getActivityLogsById(@PathVariable Long id ){
        ActivityLogsRes resp = activityLogService.getActivityLogsById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityLogsRes>> getAllActivityLogs() {
        List<ActivityLogsRes> commentResList = activityLogService.getAllActivityLogs();
        return new ResponseEntity<>(commentResList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        activityLogService.deleteActivityLogsById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
