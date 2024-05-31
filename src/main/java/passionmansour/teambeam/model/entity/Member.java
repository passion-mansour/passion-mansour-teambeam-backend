package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import passionmansour.teambeam.model.enums.StartPage;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memberId")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String mail;

    private String password;

    @Column(nullable = false)
    private String memberName;

    private String profileImage;
    // TODO: add image file

    private int notificationCount = 0;

    @Enumerated(EnumType.STRING)
    private StartPage startPage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<JoinMember> joinMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    private boolean isDeleted = false;
}
