package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import passionmansour.teambeam.model.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM message m WHERE m.project.projectId = :projectId")
    List<Message> getByProjectId(Long projectId);
}
