package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Verification {
    @Id
    @GeneratedValue
    @Column(name="verificationId")
    private Long verificationId;

    private String code;
    private String token;
    private boolean isUsed;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
