package Task_Management_service.entity;

import Task_Management_service.constant.UserRole;
import Task_Management_service.constant.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserEntity extends BaseEntity{
    String username;
    String password;
    String email;
    String mobileNo;
    @Enumerated(EnumType.STRING)
    UserRole role;
    @Enumerated(EnumType.STRING)
    UserStatus status;

}
