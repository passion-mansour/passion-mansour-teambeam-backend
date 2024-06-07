package passionmansour.teambeam.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.notification.CreateNotificationRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Notification;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.NotificationRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JwtTokenService tokenService;
    private final ProjectRepository projectRepository;

    // 알림 생성
    @Transactional
    public void saveNotification(String token, Long projectId, CreateNotificationRequest request) {

        Project project = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        Member member = tokenService.getMemberByToken(token);

        Notification notification = new Notification();
        notification.setNotificationContent(request.getNotificationContent());
        notification.setRead(false);
        notification.setProject(project);
        notification.setMember(member);

        notificationRepository.save(notification);

        List<Notification> notificationList = updateNotificationCount(token);

        log.info("notificationList {}", notificationList);

    }

    // 알림 리스트 조회
    public List<NotificationDto> getList(String token) {

        Member member = tokenService.getMemberByToken(token);

        List<Notification> notificationList = notificationRepository.
            findByMember_memberId(member.getMemberId());

        return notificationList.stream().map(this::convertToDto).collect(Collectors.toList());

    }

    private NotificationDto convertToDto(Notification notification) {

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProjectId(notification.getProject().getProjectId());
        notificationDto.setProjectName(notification.getProject().getProjectName());
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(notification.getNotificationContent());
        notificationDto.setRead(notification.isRead());

        return notificationDto;
    }

    // 알림 읽음 처리
    @Transactional
    public List<NotificationDto> updateNotification(String token, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found with notificationId: " + notificationId));

        notification.setRead(true);
        log.info("notification {}", notification);

        List<Notification> notificationList = updateNotificationCount(token);

        return notificationList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 알림 삭제
    @Transactional
    public List<NotificationDto> deleteNotification(String token, Long notificationId) {

        try {
            notificationRepository.deleteById(notificationId);

            List<Notification> notificationList = updateNotificationCount(token);

            return notificationList.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while deleting notification", e);
            throw e; // 트랜잭션 롤백을 위해 예외를 다시 던짐
        }

    }

    // 알림 수 재설정, 리스트 반환
    private List<Notification> updateNotificationCount(String token) {
        Member member = tokenService.getMemberByToken(token);

        List<Notification> notificationList = notificationRepository
            .findByMember_memberId(member.getMemberId());

        // 멤버 알림 수 재설정
        member.setNotificationCount(notificationList.size());

        log.info("member {}", member);

        return notificationList;
    }

}
