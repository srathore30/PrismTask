package Task_Management_service.repository;

import Task_Management_service.entity.AssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigneeRepository extends JpaRepository<AssigneeEntity, Long> {
}
