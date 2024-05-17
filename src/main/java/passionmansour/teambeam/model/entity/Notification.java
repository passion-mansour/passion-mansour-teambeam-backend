package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table @Data
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

    @OneToOne
    @JoinColumn(name = "projectId")
    private Project project;
}
