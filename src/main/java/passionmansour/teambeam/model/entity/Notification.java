package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table @Data
@SQLDelete(sql = "UPDATE notification SET is_deleted = true WHERE notification_id = ?")
@SQLRestriction("is_deleted = false")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notificationId")
    private Long notificationId;

    private String notificationContent;
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    private boolean is_deleted = false;
}
