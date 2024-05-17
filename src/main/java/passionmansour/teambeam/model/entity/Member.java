package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import passionmansour.teambeam.model.enums.StartPage;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table @Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memberId")
    private Long memberId;

    private String mail;
    private String password;
    private String memberName;
    // TODO: add image file
    private int notificationCount;

    @Enumerated(EnumType.STRING)
    private StartPage startPage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Verification> verifications = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<JoinMember> joinMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Memo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
}
