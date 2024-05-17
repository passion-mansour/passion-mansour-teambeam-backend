package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table @Data
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
}
