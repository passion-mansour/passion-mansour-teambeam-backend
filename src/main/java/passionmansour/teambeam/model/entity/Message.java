package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
@SQLDelete(sql = "UPDATE message SET is_deleted = true WHERE message_id = ?")
@SQLRestriction("is_deleted = false")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="messageId")
    private Long messageId;

    private String messageContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "message")
    private List<MessageComment> messageComments = new ArrayList<>();

    private boolean is_deleted = false;
}
