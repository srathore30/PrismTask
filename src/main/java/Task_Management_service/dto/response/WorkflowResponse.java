package Task_Management_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkflowResponse {
    Long workflowId;
    String name;
    Long projectId;
}
