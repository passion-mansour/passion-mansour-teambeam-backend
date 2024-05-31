package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import passionmansour.teambeam.model.entity.MessageComment;

import java.util.List;

public interface MessageCommentRepository extends JpaRepository<MessageComment, Long> {
    @Query("SELECT m FROM message_comment m WHERE m.message.messageId = :messageId")
    List<MessageComment> getByMessageId(Long messageId);
}
