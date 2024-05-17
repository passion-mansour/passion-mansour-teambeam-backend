package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import passionmansour.teambeam.model.enums.StartPage;

@Entity
@Table
public class Member {
    @Id
    @GeneratedValue
    @Column(name="memberId")
    private Long memberId;

    private String mail;
    private String password;
    private String memberName;
    // TODO: add image file
    private int notificationCount;

    @Enumerated(EnumType.STRING)
    private StartPage startPage;
}
