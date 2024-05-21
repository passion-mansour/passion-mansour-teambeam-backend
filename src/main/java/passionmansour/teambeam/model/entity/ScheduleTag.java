package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
public class ScheduleTag {

    @Id
    @GeneratedValue
    private Long scheduleTagId;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
