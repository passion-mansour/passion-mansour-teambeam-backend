package passionmansour.teambeam.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Builder
@Entity
@Table @Data
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE message_comment SET is_deleted = true WHERE message_comment_id = ?")
@SQLRestriction("is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class MessageComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageCommentId;

    @Column(length = 50000)
    private String messageCommentContent;

    private String createDate;

    private String updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;

    private boolean is_deleted = false;

    @PrePersist
    public void onPrePersist() {
        this.createDate = getCurrentDateTime();
        this.updateDate = getCurrentDateTime();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updateDate = getCurrentDateTime();
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.now().format(formatter);
    }
}
