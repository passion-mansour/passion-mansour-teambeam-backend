package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;
import passionmansour.teambeam.model.enums.MemberRole;

import java.util.Date;

@Entity
@Table
public class Project {
    @Id
    @GeneratedValue
    @Column(name="projectId")
    private Long projectId;

    private String projectName;
    private String description;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
}
