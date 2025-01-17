package Task_Management_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "team_entity")
public class TeamEntity extends BaseEntity{
    @Column(name = "username", nullable = false)
    String username;
    String teamName;
    String description;

}
