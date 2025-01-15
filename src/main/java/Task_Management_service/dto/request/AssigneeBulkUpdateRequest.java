package Task_Management_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssigneeBulkUpdateRequest {
    List<AssigneeUpdateRequest> assignees;
    List<Long> assigneeIds;
}
