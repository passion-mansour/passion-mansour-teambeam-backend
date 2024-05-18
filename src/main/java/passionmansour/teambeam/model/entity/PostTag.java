package passionmansour.teambeam.model.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class PostTag {
    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tagId")
    private Tag tag;
}
