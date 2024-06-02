package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table @Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE message SET is_deleted = true WHERE message_id = ?")
@SQLRestriction("is_deleted = false")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="messageId")
    private Long messageId;

    private String messageContent;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(mappedBy = "message")
    private List<MessageComment> messageComments = new ArrayList<>();

    private boolean is_deleted = false;

}
