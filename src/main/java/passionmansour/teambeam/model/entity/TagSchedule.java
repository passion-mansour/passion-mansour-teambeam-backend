package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class TagSchedule {
    @Id
    @GeneratedValue
    @Column(name="tagScheduleId")
    private Long tagScheduleId;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;
}
