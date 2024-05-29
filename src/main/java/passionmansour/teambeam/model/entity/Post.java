package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.PostType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="postId")
    private Long postId;

    private String postTitle;
    @Lob
    private String postContent;
    private PostType postType;
    private boolean notice;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "BoardId")
    private Board board;

    @OneToMany(mappedBy = "post")
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostComment> postComments = new ArrayList<>();
}
