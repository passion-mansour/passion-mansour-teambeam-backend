package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleTagId;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
