package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
public class TopTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="topTodoId")
    private Long topTodoId;

    private String topTodoTitle;
    private boolean topTodoStatus;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "calendarId")
    private Calendar calendar;

    @OneToMany(mappedBy = "topTodo", cascade = CascadeType.ALL)
    private List<MiddleTodo> middleTodos = new ArrayList<>();
}
