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
@SQLDelete(sql = "UPDATE middle_todo SET is_deleted = true WHERE middle_todo_id = ?")
@SQLRestriction("is_deleted = false")
public class MiddleTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="middleTodoId")
    private Long middleTodoId;

    private String middleTodoTitle;
    private boolean middleTodoStatus = false;

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

    @OneToMany(mappedBy = "middleTodo", cascade = CascadeType.ALL)
    private List<BottomTodo> bottomTodos = new ArrayList<>();

    private boolean is_deleted = false;
}
