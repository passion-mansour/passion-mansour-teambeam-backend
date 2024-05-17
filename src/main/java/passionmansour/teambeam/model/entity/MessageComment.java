package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table @Data
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
}
