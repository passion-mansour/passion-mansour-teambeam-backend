package passionmansour.teambeam.controller.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.request.MessageCommentRequest;
import passionmansour.teambeam.model.dto.message.request.MessageRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.model.dto.notification.NotificationSocketDto;
import passionmansour.teambeam.model.dto.notification.UpdateReadStatusRequest;
import passionmansour.teambeam.service.message.MessageCommentService;
import passionmansour.teambeam.service.message.MessageService;
import passionmansour.teambeam.service.notification.NotificationService;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler {

    private final SocketIOServer server;
    private final MessageService messageService;
    private final MessageCommentService messageCommentService;
    private final NotificationService notificationService;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        registerListeners(server);
    }

    public void registerListeners(SocketIOServer server) {
        log.info("Registering server listeners...");
        server.addConnectListener(onConnect());
        server.addDisconnectListener(onDisconnect());
        server.addEventListener("message", MessageRequest.class, onMessage());
        server.addEventListener("joinRoom", String.class, onJoinRoom());
        server.addEventListener("leaveRoom", String.class, onLeaveRoom());
        server.addEventListener("comment", MessageCommentRequest.class, onAddComment());
        server.addEventListener("joinMessageRoom", String.class, onJoinMessageRoom());
        server.addEventListener("updateReadStatus", UpdateReadStatusRequest.class, onUpdateReadStatus());
        server.addEventListener("deleteAll", String.class, onDeleteAll());
    }

    @OnConnect
    public ConnectListener onConnect() {
        return (client) -> {
            // 사용자의 소켓 아이디 저장
            String memberId = client.getHandshakeData().getSingleUrlParam("memberId");
            String sessionId = client.getSessionId().toString();

            if (memberId == null) {
                log.error("Null userId received from client handshake data.");
                return;
            }

            redisTemplate.opsForValue().set("USER_SOCKET_" + memberId, sessionId);
            log.info("Socket ID[{}] Connected to socket", client.getSessionId().toString());

            // 초기 데이터 전송
            try {
                notificationService.getNotificationsForMember(Long.valueOf(memberId));
            } catch (NumberFormatException e) {
                log.error("Invalid userId format: {}", memberId, e);
            }
        };
    }

    public DisconnectListener onDisconnect() {
        return client -> {
            String sessionId = client.getSessionId().toString();
            // 모든 키를 조회하여 해당 세션 ID를 가진 사용자 ID를 삭제
            redisTemplate.keys("USER_SOCKET_*").forEach(key -> {
                if (sessionId.equals(redisTemplate.opsForValue().get(key))) {
                    redisTemplate.delete(key);
                }
            });
            log.info("Socket ID[{}]  Disconnected from socket", client.getSessionId().toString());
        };
    }

    public DataListener<MessageRequest> onMessage() {
        return (client, data, ackRequest) -> {
            log.info("Received message to send: {} in project: {}", data.getMessageContent(), data.getProjectId());

            // 메시지 저장
            MessageDTO savedMessage = messageService.createMessage(data.getProjectId(), data);

            // 저장된 메시지를 해당 프로젝트의 룸에 브로드캐스트
            server.getRoomOperations(String.valueOf(data.getProjectId())).sendEvent("message", savedMessage);
        };
    }

    //입장시 기존 메시지들을 가져옴.
    public DataListener<String> onJoinRoom() {
        return (client, projectId, ackRequest) -> {
            client.joinRoom(projectId);
            log.info("Client {} joined room: {}", client.getSessionId(), projectId);

            // 채팅방 메시지 불러오기
            List<MessageDTO> messages = messageService.getMessages(Long.valueOf(projectId));
            client.sendEvent("initialMessages", messages);
        };
    }

    public DataListener<String> onLeaveRoom() {
        return (client, projectId, ackRequest) -> {
            client.leaveRoom(projectId);
            log.info("Client {} left room: {}", client.getSessionId(), projectId);
        };
    }

    public DataListener<String> onJoinMessageRoom() {
        return (client, roomId, ackRequest) -> {
            client.joinRoom(roomId);
            log.info("Client {} joined room: {}", client.getSessionId(), roomId);
            String messageId = roomId.replace("message_", "");
            List<MessageCommentDTO> comment = messageCommentService.getAllComment(Long.valueOf(messageId));
            client.sendEvent("initialComment", comment);
        };
    }


    public DataListener<MessageCommentRequest> onAddComment() {
        return (client, data, ackRequest) -> {
            log.info("Received comment: {} for message: {}", data.getMessageComment(), data.getMessageId());

            // 댓글 저장
            MessageCommentDTO updatedMessage = messageCommentService.createComment(data.getMessageId(), data.getProjectId(), data.getMessageComment(), data.getToken());

            String roomId = "message_" + data.getMessageId();

            // 업데이트된 메시지를 해당 프로젝트의 룸에 브로드캐스트
            server.getRoomOperations(roomId).sendEvent("comment", updatedMessage);
        };
    }

    public void sendNotificationToUser(NotificationSocketDto notification) {
        String socketId = (String) redisTemplate.opsForValue().get("USER_SOCKET_" + notification.getMemberId());
        if (socketId != null) {
            if (notification.getNotificationList() != null) {
                // 알림이 리스트인 경우
                server.getClient(UUID.fromString(socketId)).sendEvent("initial_notifications", notification);
                log.info("Sent list of notifications to member [{}] with socket ID [{}]", notification.getMemberId(), socketId);
            } else if (notification.getNotification() != null) {
                // 알림이 리스트가 아닌 경우
                server.getClient(UUID.fromString(socketId)).sendEvent("notification", notification);
                log.info("Sent single notification to member [{}] with socket ID [{}]", notification.getMemberId(), socketId);
            }
        } else {
            log.info("No active socket found for member [{}]", notification.getMemberId());
        }
    }

    // 읽음 처리
    public DataListener<UpdateReadStatusRequest> onUpdateReadStatus() {
        return (client, data, ackRequest) -> {
            log.info("Received notificationId {}", data.getNotificationId());
            List<NotificationDto> notificationDtoList = notificationService.updateReadStatus(data.getMemberId(), data.getNotificationId());

            NotificationSocketDto notificationSocketDto = new NotificationSocketDto(data.getMemberId(), null, notificationDtoList);
            sendNotificationToUser(notificationSocketDto);
        };
    }

    // 전체 삭제
    public DataListener<String> onDeleteAll() {
        return (client, data, ackRequest) -> {
            String result = notificationService.deleteAll();
            log.info(result);
        };
    }

}

