package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import passionmansour.teambeam.model.enums.MemberRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="projectId")
    private Long projectId;

    private String projectName;
    private String description;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<JoinMember> joinMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TopTodo> topTodos = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "calendarId")
    private Calendar calendar;

    @OneToOne
    @JoinColumn(name = "message")
    private Message message;
}
