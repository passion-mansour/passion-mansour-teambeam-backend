package passionmansour.teambeam.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import passionmansour.teambeam.service.chat.MessageService;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/messages/{projectId}")
    public List<Object> getMessages(@PathVariable String projectId) {
        return messageService.getMessages(projectId);
    }
}
