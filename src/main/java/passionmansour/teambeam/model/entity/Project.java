package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import passionmansour.teambeam.model.enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE project_id = ?")
@SQLRestriction("is_deleted = false")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;

    private String description;

    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<JoinMember> joinMembers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TopTodo> topTodos = new ArrayList<>();

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Calendar calendar;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Message> message;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    private boolean is_deleted = false;
}