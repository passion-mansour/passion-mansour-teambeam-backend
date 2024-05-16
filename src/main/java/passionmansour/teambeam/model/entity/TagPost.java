package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class TagPost {
    @Id
    @GeneratedValue
    @Column(name="tagPostId")
    private Long tagPostId;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
}
