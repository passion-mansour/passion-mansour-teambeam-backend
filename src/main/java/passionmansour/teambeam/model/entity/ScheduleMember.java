package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class ScheduleMember {
    @Id
    @GeneratedValue
    @Column(name="scheduleMemberId")
    private Long scheduleMemberId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
}
