package Task_Management_service.entity;

import Task_Management_service.constant.TaskPriority;
import Task_Management_service.constant.TaskStatus;
import Task_Management_service.constant.TaskType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskEntity extends BaseEntity {
    String title;
    String description;
    TaskStatus status;
    TaskPriority priority;
    TaskType type;

    Date dueDate;

    Date createdAt;
}
