package Task_Management_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssigneeResponse {
    Long id;
    String title;
    String description;
    Long taskId;
    String taskName;
    Long userId;
    String userName;
}
