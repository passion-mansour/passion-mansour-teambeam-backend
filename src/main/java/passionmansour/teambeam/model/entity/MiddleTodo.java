package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class MiddleTodo {
    @Id
    @GeneratedValue
    @Column(name="middleTodoId")
    private Long middleTodoId;

    private String middleTodoTitle;
    private boolean middleTodoStatus;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "topTodoId")
    private TopTodo topTodo;
}
