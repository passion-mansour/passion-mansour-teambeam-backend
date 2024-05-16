package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class MessageComment {
    @Id
    @GeneratedValue
    @Column(name="messageCommentId")
    private Long messageCommentId;

    @Lob
    private String messageCommentContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne
    @JoinColumn(name = "messageId")
    private Message message;
}
