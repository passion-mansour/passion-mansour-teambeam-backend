package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table @Data
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    private List<TopTodo> topTodos = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "calendar", cascade = CascadeType.ALL)
    private Project project;
}
