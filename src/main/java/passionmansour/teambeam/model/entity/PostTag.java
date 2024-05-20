package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
public class PostTag {
    @Id
    @GeneratedValue
    private Long postTagId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
