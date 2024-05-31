package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import passionmansour.teambeam.model.enums.TagCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tag SET is_deleted = true WHERE tag_id = ?")
@SQLRestriction("is_deleted = false")
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

    private boolean is_deleted = false;
}
