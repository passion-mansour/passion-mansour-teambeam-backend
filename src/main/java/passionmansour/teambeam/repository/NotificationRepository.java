package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 멤버의 특정 프로젝트 알림 조회
    List<Notification> findByMember_memberId(Long memberId);

}
