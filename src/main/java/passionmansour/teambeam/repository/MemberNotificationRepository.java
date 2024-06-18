package passionmansour.teambeam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passionmansour.teambeam.model.entity.MemberNotification;

import java.util.List;
import java.util.Optional;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {
    Optional<MemberNotification> findByMember_memberIdAndNotification_notificationId(Long memberId, Long notificationId);

    List<MemberNotification> findByMember_memberId(Long memberId);

    long countByNotification_notificationId(Long notificationId);
}
