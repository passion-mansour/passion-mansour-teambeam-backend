package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Entity
@Table @Data
@SQLDelete(sql = "UPDATE message_comment SET is_deleted = true WHERE message_comment_id = ?")
@SQLRestriction("is_deleted = false")
public class MessageComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="messageCommentId")
    private Long messageCommentId;

    @Lob
    private String messageCommentContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;

    private boolean is_deleted = false;
}
