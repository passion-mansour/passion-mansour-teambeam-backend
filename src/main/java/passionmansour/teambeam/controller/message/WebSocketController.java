/*
package passionmansour.teambeam.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.request.MessageCommentRequest;
import passionmansour.teambeam.model.dto.message.request.MessageRequest;
import passionmansour.teambeam.model.entity.MessageComment;
import passionmansour.teambeam.service.chat.MessageCommentService;
import passionmansour.teambeam.service.chat.MessageService;

import java.util.List;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final MessageCommentService messageCommentService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService, MessageCommentService messageCommentService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.messageCommentService = messageCommentService;
    }

    //처음 채팅방 열기
    @MessageMapping("/chat.loadMessages/{projectId}")
    public void loadMessages(@DestinationVariable Long projectId) {
        List<MessageDTO> messages = messageService.getMessages(projectId);
        messagingTemplate.convertAndSend("/topic/" + projectId, messages);
    }

    //메시지 보내기
    @MessageMapping("/chat.sendMessage/{projectId}")
    public void sendMessage(@DestinationVariable Long projectId, MessageRequest messageRequest) {
        MessageDTO message = messageService.createMessage(projectId, messageRequest);
        messagingTemplate.convertAndSend("/topic/" + projectId, message);
    }

    //메시지 코멘트 방 열기 => 원래 있던 데이터들을 받을 수 있다.
    @MessageMapping("/chat.openComments/{messageId}")
    public void openComments(@DestinationVariable Long messageId) {
        List<MessageCommentDTO> comments = messageCommentService.getCommentsByMessageId(messageId);
        messagingTemplate.convertAndSend("/topic/comments/" + messageId, comments);
    }

    //메시지에 코멘트 달기
    @MessageMapping("/chat.sendComment/{messageId}")
    public void sendComment(@DestinationVariable Long messageId, MessageCommentRequest messageCommentRequest) {
        MessageCommentDTO comment = messageCommentService.saveComment(messageId, messageCommentRequest);
        messagingTemplate.convertAndSend("/topic/comment/" + messageId, comment);
    }
}
*/
