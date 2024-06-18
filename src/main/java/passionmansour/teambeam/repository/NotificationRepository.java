package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 프로젝트의 공지 알림 조회
    List<Notification> findByProject_projectIdAndType(Long projectId, Notification.Type type);
}
