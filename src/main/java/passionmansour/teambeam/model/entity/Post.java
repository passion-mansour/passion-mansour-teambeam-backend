package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import passionmansour.teambeam.model.enums.PostType;

import java.util.Date;

@Entity
@Table
public class Post {
    @Id
    @GeneratedValue
    @Column(name="postId")
    private Long postId;

    private String postTitle;
    @Lob
    private String postContent;
    private PostType postType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}
