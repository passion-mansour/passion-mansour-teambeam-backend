package passionmansour.teambeam.service.notification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.controller.message.MessageHandler;
import passionmansour.teambeam.model.dto.notification.CreateNotificationRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.model.dto.notification.NotificationSocketDto;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.BottomTodoRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.NotificationRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDate;
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
    private final MemberRepository memberRepository;

    // 알림 생성
    @Transactional
    public void saveNotification(String token, Long projectId, CreateNotificationRequest request) {
        Member member = tokenService.getMemberByToken(token);

        MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);

        Project project = projectRepository.findByProjectId(projectId)
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + projectId));

        Notification notification = new Notification();
        notification.setNotificationContent(request.getNotificationContent());
        notification.setRead(false);
        notification.setType(Notification.Type.NOTICE);
        notification.setProject(project);
        notification.setBoardId(request.getBoardId());
        notification.setPostId(request.getPostId());

        notificationRepository.save(notification);
        log.info("Saved notification for project ID: {}", projectId);

        NotificationDto notificationDto = convertToDto(notification);

        List<JoinMember> joinMembers = project.getJoinMembers();

        for (JoinMember joinMember : joinMembers) {
            Long memberId = joinMember.getMember().getMemberId();
            NotificationSocketDto notificationSocketDto = new NotificationSocketDto(memberId, notificationDto, null);
            messageHandler.sendNotificationToUser(notificationSocketDto);
            log.info("Event notification {}", notificationDto);
        }
    }

    private NotificationDto convertToDto(Notification notification) {

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProjectId(notification.getProject().getProjectId());
        notificationDto.setProjectName(notification.getProject().getProjectName());
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setNotificationContent(notification.getNotificationContent());
        notificationDto.setRead(notification.isRead());
        notificationDto.setType(notification.getType());
        notificationDto.setBoardId(notification.getBoardId());
        notificationDto.setPostId(notification.getPostId());

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

    // SOCKET 알림 읽음 처리
    @Transactional
    public List<NotificationDto> updateReadStatus(Long memberId, Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found with notificationId: " + notificationId));

        notification.setRead(true);
        log.info("notification {}", notification);

        List<Notification> notificationList = notificationRepository.findByMember_memberId(memberId);
        return notificationList.stream().map(this::convertToDto).toList();
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

    // SOCKET 알림 삭제
    @Transactional
    public String deleteAll() {
        notificationRepository.deleteAll();
        return "Delete All Notifications Successfully";
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
        LocalDate today = LocalDate.now();
        List<BottomTodo> todoList = todoRepository.findByEndDate(today);
        log.info("todoList {}", todoList.size());

        createNotification(todoList);
    }

    public void createNotification(List<BottomTodo> todoList) {
        LocalDate today = LocalDate.now();

        for (BottomTodo todo : todoList) {
            LocalDate endDate = todo.getEndDate();

            // 날짜 비교 후 저장
            if (endDate.equals(today)) {
                String notice = todo.getBottomTodoTitle() + "이(가) 오늘 마감입니다!";

                Notification notification = new Notification();
                notification.setNotificationContent(notice);
                notification.setRead(false);
                notification.setType(Notification.Type.TODO);
                notification.setProject(todo.getProject());
                notification.setMember(todo.getMember());

                notificationRepository.save(notification);
                log.info("notification {}", notification);

                NotificationSocketDto notificationSocketDto = new NotificationSocketDto(todo.getMember().getMemberId(), convertToDto(notification), null);
                MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);
                messageHandler.sendNotificationToUser(notificationSocketDto);

            }
        }
    }

    // 해당 프로젝트의 공지 알림 리스트 조회
    public List<NotificationDto> getNotificationsByProjectId(Long projectId) {
        log.info("Executing query to fetch notifications for project {}", projectId);
        List<Notification> notificationList = notificationRepository.findByProject_projectIdAndType(projectId, Notification.Type.NOTICE);
        log.info("Query result: {} notifications found", notificationList.size());
        return notificationList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // API 사용자의 모든 알림 조회
    public List<NotificationDto> getNotificationsForMember(String token) {

        Member member = tokenService.getMemberByToken(token);

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

    // SOCKET 사용자의 모든 알림 조회
    public void getNotificationsForMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("Member not found with memberId: " + memberId));

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

        List<NotificationDto> notificationList = allNotifications.stream().map(this::convertToDto).collect(Collectors.toList());

        NotificationSocketDto notificationSocketDto = new NotificationSocketDto(Long.valueOf(memberId), null, notificationList);
        MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);
        messageHandler.sendNotificationToUser(notificationSocketDto);
    }
}
