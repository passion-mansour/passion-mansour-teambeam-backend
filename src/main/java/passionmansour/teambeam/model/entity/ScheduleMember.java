package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table @Data
@SQLDelete(sql = "UPDATE schedule_member SET is_deleted = true WHERE schedule_member_id = ?")
@SQLRestriction("is_deleted = false")
public class ScheduleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="scheduleMemberId")
    private Long scheduleMemberId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    private boolean is_deleted = false;
}
