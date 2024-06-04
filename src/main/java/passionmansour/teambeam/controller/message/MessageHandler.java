package passionmansour.teambeam.controller.message;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.request.MessageCommentRequest;
import passionmansour.teambeam.model.dto.message.request.MessageRequest;
import passionmansour.teambeam.service.message.MessageCommentService;
import passionmansour.teambeam.service.message.MessageService;

import java.util.List;

@Component
@Slf4j
public class MessageHandler {

    private SocketIOServer server;
    private final MessageService messageService;
    private final MessageCommentService messageCommentService;

    @Autowired
    public MessageHandler(SocketIOServer server, MessageService messageService, MessageCommentService messageCommentService) {
        this.messageService = messageService;
        this.messageCommentService = messageCommentService;
        this.server = server;
        registerListeners(server);
    }

    public void registerListeners(SocketIOServer server) {
        server.addConnectListener(onConnect());
        server.addDisconnectListener(onDisconnect());
        server.addEventListener("message", MessageRequest.class, onMessage());
        server.addEventListener("joinRoom", String.class, onJoinRoom());
        server.addEventListener("leaveRoom", String.class, onLeaveRoom());
        server.addEventListener("addComment", MessageCommentRequest.class, onAddComment());
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


    public DataListener<MessageCommentRequest> onAddComment() {
        return (client, data, ackRequest) -> {
            log.info("Received comment: {} for message: {}", data.getMessageComment(), data.getMessageId());

            // 댓글 저장
            MessageDTO updatedMessage = messageCommentService.createComment(data.getMessageId(), data.getProjectId(), data.getMessageComment(), data.getToken());

            // 업데이트된 메시지를 해당 프로젝트의 룸에 브로드캐스트
            server.getRoomOperations(String.valueOf(data.getProjectId())).sendEvent("message", updatedMessage);
        };
    }
}
