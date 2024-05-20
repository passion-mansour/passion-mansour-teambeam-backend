package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import passionmansour.teambeam.model.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue
    private Long projectId;

    private String projectName;

    private String description;

    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<JoinMember> joinMemberList;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<JoinMember> joinMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TopTodo> topTodos = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendarId")
    private Calendar calendar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message")
    private Message message;
}
