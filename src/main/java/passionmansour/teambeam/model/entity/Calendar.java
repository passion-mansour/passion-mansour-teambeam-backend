package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Calendar {
    @Id
    @GeneratedValue
    @Column(name="calenderId")
    private Long calenderId;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "topTodoId")
    private TopTodo topTodo;
}
