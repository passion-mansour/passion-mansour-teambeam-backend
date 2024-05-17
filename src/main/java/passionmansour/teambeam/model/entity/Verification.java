package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table @Data
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
