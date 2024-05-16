package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Notification {
    @Id
    @GeneratedValue
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
