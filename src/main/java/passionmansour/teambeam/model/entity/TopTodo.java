package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class TopTodo {
    @Id
    @GeneratedValue
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
}
