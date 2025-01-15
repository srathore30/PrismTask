package Task_Management_service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportUpdateRequest {
    Long id;
    String title;
    String description;
    Long teamId;
}
