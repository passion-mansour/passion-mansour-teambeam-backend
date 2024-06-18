package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MemberNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notificationId")
    private Notification notification;

    private boolean isRead;
}
