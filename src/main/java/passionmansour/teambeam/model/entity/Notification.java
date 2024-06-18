package passionmansour.teambeam.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table @Data
@SQLDelete(sql = "UPDATE notification SET is_deleted = true WHERE notification_id = ?")
@SQLRestriction("is_deleted = false")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notificationId")
    private Long notificationId;

    private String notificationContent;

    private boolean is_deleted = false;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long boardId;

    public enum Type {
        TODO, NOTICE,
    }
}
