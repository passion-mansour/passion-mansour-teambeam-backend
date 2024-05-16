package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table
public class BottomTodo {
    @Id
    @GeneratedValue
    @Column(name="bottomTodoId")
    private Long bottomTodoId;

    private String bottomTodoTitle;
    private boolean bottomTodoStatus;
    private String memo;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "middleTodoId")
    private MiddleTodo middleTodo;
}
