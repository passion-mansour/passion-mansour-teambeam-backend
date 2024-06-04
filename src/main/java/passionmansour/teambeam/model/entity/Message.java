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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table @Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE message SET is_deleted = true WHERE message_id = ?")
@SQLRestriction("is_deleted = false")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(length = 50000)
    private String messageContent;

    @CreatedDate
    private String createDate;

    @LastModifiedDate
    private String updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(mappedBy = "message")
    private List<MessageComment> messageComments = new ArrayList<>();

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
