package Task_Management_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tb_workflow_steps")
public class WorkflowStepEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    WorkflowEntity workflow;
    String name;
    int position;
}
