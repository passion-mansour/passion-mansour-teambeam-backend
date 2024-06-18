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
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDate;
import java.util.*;

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
    private final MemberNotificationRepository memberNotificationRepository;

    // 알림 생성
    @Transactional
    public NotificationSocketDto saveNotification(CreateNotificationRequest request) {

        Project project = projectRepository.findByProjectId(request.getProjectId())
            .orElseThrow(() -> new EntityNotFoundException("Project not found with projectId: " + request.getProjectId()));

        Notification notification = new Notification();
        notification.setNotificationContent(request.getTitle());
        notification.setType(Notification.Type.NOTICE);
        notification.setProject(project);
        notification.setBoardId(request.getBoardId());
        notification.setPostId(request.getPostId());

        List<JoinMember> joinMembers = project.getJoinMembers();

        notificationRepository.save(notification);
        log.info("Saved notification for project ID: {}", request.getProjectId());

        for (JoinMember joinMember : joinMembers) {
            Member member = joinMember.getMember();

            MemberNotification memberNotification = new MemberNotification();
            memberNotification.setMember(member);
            memberNotification.setNotification(notification);
            memberNotification.setRead(false);

            memberNotificationRepository.save(memberNotification);
        }

        return new NotificationSocketDto(request.getMemberId(), convertToDto(request.getMemberId(), notification), null);

    }

    private NotificationDto convertToDto(Long memberId, Notification notification) {

        MemberNotification memberNotification = memberNotificationRepository.findByMember_memberIdAndNotification_notificationId(memberId, notification.getNotificationId())
            .orElseThrow(() -> new EntityNotFoundException("MemberNotification not found with notificationId" + notification.getNotificationId()));

        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setProjectId(notification.getProject().getProjectId());
        notificationDto.setProjectName(notification.getProject().getProjectName());
        notificationDto.setNotificationId(notification.getNotificationId());
        notificationDto.setTitle(notification.getNotificationContent());
        notificationDto.setRead(memberNotification.isRead());
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

        log.info("notification {}", notification);

        Member member = tokenService.getMemberByToken(token);

        List<Notification> notificationList = updateNotificationCount(token);

        List<NotificationDto> response = new ArrayList<>();
        for (Notification item : notificationList) {
            NotificationDto notificationDto = convertToDto(member.getMemberId(), item);
            response.add(notificationDto);
        }
        return response;
    }

    // SOCKET 알림 읽음 처리
    @Transactional
    public void updateReadStatus(Long memberId, Long notificationId) {

        MemberNotification memberNotification = memberNotificationRepository.findByMember_memberIdAndNotification_notificationId(memberId, notificationId)
            .orElseThrow(() -> new EntityNotFoundException("MemberNotification not found with notificationId" + notificationId));

        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("Notification not found with notificationId: " + notificationId));

        memberNotification.setRead(true);
        log.info("notification {}", notification);

        getNotificationsForMember(memberId);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(String token, Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // SOCKET 알림 삭제
    @Transactional
    public void deleteAll(Long memberId) {
        List<MemberNotification> memberNotifications = memberNotificationRepository.findByMember_memberId(memberId);
        // 멤버 ID와 알림 ID를 기반으로 MemberNotification 조회 및 삭제
        for (MemberNotification memberNotification : memberNotifications) {
            memberNotificationRepository.delete(memberNotification);

            // 남은 MemberNotification이 있는지 확인
            long count = memberNotificationRepository.countByNotification_notificationId(memberNotification.getNotification().getNotificationId());
                if (count == 0) {
                    // 남은 MemberNotification이 없으면 해당 알림 삭제
                    notificationRepository.deleteById(memberNotification.getNotification().getNotificationId());
                }
            }
    }

    // 사용자의 알림 수 재설정, 리스트 반환
    private List<Notification> updateNotificationCount(String token) {
        Member member = tokenService.getMemberByToken(token);

        List<MemberNotification> memberNotifications = memberNotificationRepository
            .findByMember_memberId(member.getMemberId());

        // 멤버 알림 수 재설정
        member.setNotificationCount(memberNotifications.size());
        log.info("member {}", member);

        List<Notification> notificationList = new ArrayList<>();

        for (MemberNotification memberNotification : memberNotifications) {
            Notification notification = notificationRepository.findById(memberNotification.getNotification().getNotificationId())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with notificationId" + memberNotification.getNotification().getNotificationId()));
            notificationList.add(notification);
        }

        return notificationList;
    }

    // 프로젝트에 참여한 모든 멤버의 알림 수를 재설정
    @Transactional
    private void updateNotificationCountForProjectMembers(List<JoinMember> joinMembers) {
        List<Notification> allNotifications = new ArrayList<>();

        for (JoinMember member : joinMembers) {
            List<MemberNotification> memberNotifications = memberNotificationRepository.findByMember_memberId(member.getMember().getMemberId());

            // 멤버 알림 수 재설정
            member.getMember().setNotificationCount(memberNotifications.size());
        }

        log.info("All members updated: {}", joinMembers.size());
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
                notification.setType(Notification.Type.TODO);
                notification.setProject(todo.getProject());

                notificationRepository.save(notification);
                log.info("notification {}", notification);

                MemberNotification memberNotification = new MemberNotification();
                memberNotification.setMember(todo.getMember());
                memberNotification.setNotification(notification);
                memberNotification.setRead(false);

                memberNotificationRepository.save(memberNotification);
                log.info("saved memberNotification {}", memberNotification);


                NotificationSocketDto notificationSocketDto = new NotificationSocketDto(todo.getMember().getMemberId(),
                    convertToDto(todo.getMember().getMemberId(), notification), null);

                MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);
                messageHandler.sendNotificationToUser(notificationSocketDto);

            }
        }
    }

    // API 사용자의 모든 알림 조회
    public List<NotificationDto> getNotificationsForMember(String token) {

        Member member = tokenService.getMemberByToken(token);

        List<MemberNotification> memberNotifications = memberNotificationRepository.findByMember_memberId(member.getMemberId());

        List<NotificationDto> notificationList = new ArrayList<>();
        for (MemberNotification memberNotification : memberNotifications) {
            NotificationDto notificationDto = convertToDto(member.getMemberId(), memberNotification.getNotification());
            notificationList.add(notificationDto);
        }

        return notificationList;
    }

    // SOCKET 사용자의 모든 알림 조회
    public void getNotificationsForMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("Member not found with memberId: " + memberId));

        List<MemberNotification> memberNotifications = memberNotificationRepository.findByMember_memberId(memberId);

        List<NotificationDto> notificationList = new ArrayList<>();
        for (MemberNotification memberNotification : memberNotifications) {
            NotificationDto notificationDto = convertToDto(memberId, memberNotification.getNotification());
            notificationList.add(notificationDto);
        }

        NotificationSocketDto notificationSocketDto = new NotificationSocketDto(memberId, null, notificationList);
        MessageHandler messageHandler = applicationContext.getBean(MessageHandler.class);
        messageHandler.sendNotificationToUser(notificationSocketDto);
    }
}
