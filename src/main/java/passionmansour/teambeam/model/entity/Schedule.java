package passionmansour.teambeam.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table
public class Schedule {
    @Id
    @GeneratedValue
    @Column(name="scheduleId")
    private Long scheduleId;
    private String scheduleTitle;
    private String scheduleLocate;
    private String scheduleLink;

    @Lob
    private String scheduleContent;

    @Temporal(TemporalType.TIME)
    private Date scheduleTime;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
}
