package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import passionmansour.teambeam.model.enums.TagCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table @Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tagId")
    private Long tagId;

    private String tagName;

    @Enumerated(EnumType.STRING)
    private TagCategory tagCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "tag")
    private List<ScheduleTag> scheduleTags = new ArrayList<>();

    @OneToMany(mappedBy = "tag")
    private List<TodoTag> todoTags = new ArrayList<>();
}
