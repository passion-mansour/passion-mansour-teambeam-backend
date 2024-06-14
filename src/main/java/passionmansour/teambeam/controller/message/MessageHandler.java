package passionmansour.teambeam.controller.message;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.request.MessageCommentRequest;
import passionmansour.teambeam.model.dto.message.request.MessageRequest;
import passionmansour.teambeam.model.dto.notification.NotificationDto;
import passionmansour.teambeam.service.message.MessageCommentService;
import passionmansour.teambeam.service.message.MessageService;
import passionmansour.teambeam.service.notification.NotificationService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageHandler {

    private final SocketIOServer server;
    private final MessageService messageService;
    private final MessageCommentService messageCommentService;
    private final NotificationService notificationService;

    @PostConstruct
    public void init() {
        registerListeners(server);
    }

    public void registerListeners(SocketIOServer server) {
        log.info("Registering server listeners...");
        server.addConnectListener(onConnect());
        server.addDisconnectListener(onDisconnect());
        server.addEventListener("joinProject", String.class, onJoinProject());
        server.addEventListener("message", MessageRequest.class, onMessage());
        server.addEventListener("joinRoom", String.class, onJoinRoom());
        server.addEventListener("leaveRoom", String.class, onLeaveRoom());
        server.addEventListener("comment", MessageCommentRequest.class, onAddComment());
        server.addEventListener("joinMessageRoom", String.class, onJoinMessageRoom());
    }

    @OnConnect
    public ConnectListener onConnect() {
        return (client) -> {
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    public DisconnectListener onDisconnect() {
        return client -> log.info("Socket ID[{}]  Disconnected from socket", client.getSessionId().toString());
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

    public DataListener<String> onJoinProject() {
        return (client, projectId, ackRequest) -> {
            try {
                client.joinRoom("project_announcement_" + projectId);
                log.info("Client {} joined Project: {}", client.getSessionId(), "project_announcement_" + projectId);

                List<NotificationDto> notificationList = notificationService.getNotificationsByProjectId(Long.valueOf(projectId));
                log.info("Loaded {} notifications for project {}", notificationList.size(), projectId);
                client.sendEvent("initialNotice", notificationList);
            } catch (Exception e) {
                log.error("Error while client joining project: {}", e.getMessage());
                client.sendEvent("error", "Error occurred: " + e.getMessage());
            }
        };
    }

    public void onNotificationEvent(Long projectId, NotificationDto notificationDto) {
        try {
            String room = "project_announcement_" + projectId;
            server.getRoomOperations(room).sendEvent("announcement", notificationDto);
            log.info("Sent announcement to project {}: {}", projectId, notificationDto);
        } catch (Exception e) {
            log.error("Error sending announcement", e);
        }
    }



}

