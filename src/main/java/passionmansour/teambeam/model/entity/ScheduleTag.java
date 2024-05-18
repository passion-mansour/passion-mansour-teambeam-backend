package passionmansour.teambeam.model.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ScheduleTag {
    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
