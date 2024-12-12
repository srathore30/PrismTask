package Task_Management_service.dto.request;

import Task_Management_service.constant.UserRole;
import Task_Management_service.constant.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserReqDto {
    String username;
    String password;
    String email;
    String mobileNo;
    UserRole role;
}
