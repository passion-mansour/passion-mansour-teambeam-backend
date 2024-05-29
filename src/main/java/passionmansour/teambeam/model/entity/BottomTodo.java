package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table @Data
@SQLDelete(sql = "UPDATE bottom_todo SET is_deleted = true WHERE bottom_todo_id = ?")
@SQLRestriction("is_deleted = false")
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

    @OneToMany(mappedBy = "todo")
    private List<TodoTag> todoTags = new ArrayList<>();

    private boolean is_deleted = false;
}
