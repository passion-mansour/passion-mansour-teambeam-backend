package passionmansour.teambeam.service.notification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.controller.message.MessageHandler;
import passionmansour.teambeam.model.dto.notification.CreateNotificationRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.BottomTodoRepository;
import passionmansour.teambeam.repository.NotificationRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JwtTokenService tokenService;
    private final ProjectRepository projectRepository;
    private final BottomTodoRepository todoRepository;
    private final ApplicationContext applicationContext;

    // 알림 생성
    @Transactional
    public void saveNotification(String token, Long projectId, CreateNotificationRequest request) {
        log.info("Saving notification for project ID: {}", projectId);

        Project project = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        Notification notification = new Notification();
        notification.setNotificationContent(request.getNotificationContent());
        notification.setRead(false);
        notification.setType(Notification.Type.NOTICE);
        notification.setProject(project);

        notificationRepository.save(notification);
        log.info("Saved notification for project ID: {}", projectId);

        NotificationDto notificationDto = convertToDto(notification);

        MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);
        messageHandler.onNotificationEvent(projectId, notificationDto);
        log.info("Event notification {}", notificationDto);

        log.info("Notification saved and event triggered for project ID: {}", projectId);
    }

    private NotificationDto convertToDto(Notification notification) {

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProjectId(notification.getProject().getProjectId());
        notificationDto.setProjectName(notification.getProject().getProjectName());
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(notification.getNotificationContent());
        notificationDto.setRead(notification.isRead());
        notificationDto.setType(notification.getType());

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

    // 프로젝트에 참여한 모든 멤버의 알림 수를 재설정
    @Transactional
    private List<NotificationDto> updateNotificationCountForProjectMembers(List<JoinMember> joinMembers) {
        List<Notification> allNotifications = new ArrayList<>();

        for (JoinMember member : joinMembers) {
            List<Notification> notificationList = notificationRepository.findByMember_memberId(member.getMember().getMemberId());

            // 멤버 알림 수 재설정
            member.getMember().setNotificationCount(notificationList.size());

            allNotifications.addAll(notificationList);
        }

        log.info("All members updated: {}", joinMembers.size());

        return allNotifications.stream().map(this::convertToDto).toList();
    }

    // 임박 투두 알림 생성 (매일 자정)
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveDailyNotification() {
        List<BottomTodo> todoList = todoRepository.findByEndDate(new Date());
        log.info("todoList {}", todoList.size());

        createNotification(todoList);
    }

    public void createNotification(List<BottomTodo> todoList) {

        for (BottomTodo todo : todoList) {
            String endDate = todo.getEndDate().toString();
            log.info("Todo end date: {}", endDate);

            // 날짜 비교 후 저장
            String notice = todo.getBottomTodoTitle() + "이(가) 오늘 마감입니다!";

            Notification notification = new Notification();
            notification.setNotificationContent(notice);
            notification.setRead(false);
            notification.setType(Notification.Type.TODO);
            notification.setProject(todo.getProject());
            notification.setMember(todo.getMember());

            notificationRepository.save(notification);

            log.info("notification {}", notification);
        }
    }

    // 해당 프로젝트의 공지 알림 리스트 조회
    public List<NotificationDto> getNotificationsByProjectId(Long projectId) {
        log.info("Executing query to fetch notifications for project {}", projectId);
        List<Notification> notificationList = notificationRepository.findByProject_projectIdAndType(projectId, Notification.Type.NOTICE);
        log.info("Query result: {} notifications found", notificationList.size());
        return notificationList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 사용자의 모든 알림 조회
    public List<NotificationDto> getNotificationsForMember(String token) {

        Member member = tokenService.getMemberByToken(token);

        log.info("Fetching notifications for member ID: {}", member.getMemberId());

        // 개인 알림 조회
        List<Notification> personalNotifications = notificationRepository.findByMember_memberIdAndType(member.getMemberId(), Notification.Type.TODO);
        log.info("Personal notifications found: {}", personalNotifications.size());

        // 멤버가 참여한 프로젝트를 조회
        List<Project> projects = projectRepository.findByJoinMembers_Member_MemberId(member.getMemberId());

        // 각 프로젝트에 대한 공지 알림 조회
        List<Notification> projectNotifications = projects.stream()
            .flatMap(project -> notificationRepository.findByProject_projectIdAndType(project.getProjectId(), Notification.Type.NOTICE).stream())
            .collect(Collectors.toList());
        log.info("Project notifications found: {}", projectNotifications.size());

        // 개인 알림과 프로젝트 공지 알림을 합침
        List<Notification> allNotifications = personalNotifications;
        allNotifications.addAll(projectNotifications);

        return allNotifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
