package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Memo {
    @Id
    @GeneratedValue
    @Column(name="memoId")
    private Long memoId;

    private String memoTile;

    @Lob
    private String memoContent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
