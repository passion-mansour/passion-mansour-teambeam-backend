package passionmansour.teambeam.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.service.message.MessageCommentService;
import passionmansour.teambeam.service.message.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/team")
public class MessageController {
    private final MessageService messageService;
    private final MessageCommentService messageCommentService;

    @Autowired
    public MessageController(MessageService messageService, MessageCommentService messageCommentService) {
        this.messageService = messageService;
        this.messageCommentService = messageCommentService;
    }

    @GetMapping("/chat/{projectId}")
    public List<MessageDTO> getMessages(@PathVariable Long projectId) {
        return messageService.getMessages(projectId);
    }

    @GetMapping("/chat/{projectId}/{messageId}")
    public List<MessageCommentDTO> getComment(@PathVariable Long projectId, @PathVariable Long messageId) {
        List<MessageCommentDTO> messages = messageCommentService.getAllComment(messageId);
        return messages;
    }
}
