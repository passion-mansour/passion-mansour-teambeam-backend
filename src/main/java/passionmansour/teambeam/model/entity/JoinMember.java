package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import passionmansour.teambeam.model.enums.MemberRole;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinMemberId;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private boolean isHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

}
