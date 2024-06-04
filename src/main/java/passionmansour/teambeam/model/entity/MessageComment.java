package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Entity
@Table @Data
@SQLDelete(sql = "UPDATE message_comment SET is_deleted = true WHERE message_comment_id = ?")
@SQLRestriction("is_deleted = false")
public class MessageComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="messageCommentId")
    private Long messageCommentId;

    @Column(length = 50000)
    private String messageCommentContent;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;

    private boolean is_deleted = false;
}
