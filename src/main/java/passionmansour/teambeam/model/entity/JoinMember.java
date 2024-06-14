package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE join_member SET is_deleted = true WHERE join_member_id = ?")
@SQLRestriction("is_deleted = false")
public class JoinMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinMemberId;

    private String memberRole;

    private boolean isHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    private boolean is_deleted = false;
}
