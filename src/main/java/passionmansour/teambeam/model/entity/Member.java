package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import passionmansour.teambeam.model.enums.StartPage;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memberName;

    private String profileImage;
    // TODO: add image file

    private int notificationCount;

    @Enumerated(EnumType.STRING)
    private StartPage startPage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<JoinMember> joinMemberList;
}
