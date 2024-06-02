package passionmansour.teambeam.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import passionmansour.teambeam.model.dto.chat.MessageDTO;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.service.chat.MessageService;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.sendMessage/{projectId}")
    public void sendMessage(@DestinationVariable Long projectId, MessageDTO messageDTO) {
        Message message = messageService.saveMessage(projectId, messageDTO);
        messagingTemplate.convertAndSend("/topic/" + projectId, message);
    }

}
