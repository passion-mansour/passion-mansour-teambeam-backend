package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class PostComment {
    @Id
    @GeneratedValue
    @Column(name="postCommentId")
    private Long postCommentId;

    @Lob
    private String postCommentContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
}
