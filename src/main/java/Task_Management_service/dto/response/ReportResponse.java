package Task_Management_service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    Long id;
    String title;
    String description;
    Long userId;
    Long teamId;
}
