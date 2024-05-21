package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
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

    @OneToMany(mappedBy = "middleTodo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<BottomTodo> bottomTodos = new ArrayList<>();
}
