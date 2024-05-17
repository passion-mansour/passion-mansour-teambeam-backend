package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table @Data
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookmarkId")
    private Long bookmarkId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;
}
