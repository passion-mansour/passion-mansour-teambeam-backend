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
@SQLDelete(sql = "UPDATE top_todo SET is_deleted = true WHERE top_todo_id = ?")
@SQLRestriction("is_deleted = false")
public class TopTodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="topTodoId")
    private Long topTodoId;

    private String topTodoTitle;
    private boolean topTodoStatus = false;

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

    private boolean is_deleted = false;
}
