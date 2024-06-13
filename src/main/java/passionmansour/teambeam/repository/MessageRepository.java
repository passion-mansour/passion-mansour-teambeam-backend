package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.Project;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByProject(Project project);
}
