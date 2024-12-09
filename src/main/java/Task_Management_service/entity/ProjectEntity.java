package Task_Management_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tb_projects")
public class ProjectEntity extends BaseEntity {

     String name;
     String description;
     Long teamId;

     private Long ownerId;

     Date createdAt = new Date();
}