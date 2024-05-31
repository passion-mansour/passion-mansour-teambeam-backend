package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table @Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE bookmark SET is_deleted = true WHERE bookmark_id = ?")
@SQLRestriction("is_deleted = false")
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

    @Builder.Default
    private boolean is_deleted = false;
}
