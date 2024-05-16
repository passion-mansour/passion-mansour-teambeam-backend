package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Bookmark {
    @Id
    @GeneratedValue
    @Column(name="bookmarkId")
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;
}
