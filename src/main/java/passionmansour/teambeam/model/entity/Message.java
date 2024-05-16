package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Message {
    @Id
    @GeneratedValue
    @Column(name="messageId")
    private Long messageId;

    private String messageContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
