package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table @Data
public class BottomTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bottomTodoId")
    private Long bottomTodoId;

    private String bottomTodoTitle;
    private boolean bottomTodoStatus = false;
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

    @OneToMany(mappedBy = "bottomTodo")
    private List<TodoTag> todoTags = new ArrayList<>();
}
