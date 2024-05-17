package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import passionmansour.teambeam.model.enums.MemberRole;

@Entity
@Table @Data
public class JoinMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="joinMemberId")
    private Long joinMemberId;

    private MemberRole memberRole;
    private boolean isHost;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}
