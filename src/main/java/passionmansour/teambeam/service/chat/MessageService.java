package passionmansour.teambeam.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.chat.request.PatchMessageRequest;
import passionmansour.teambeam.model.dto.chat.request.PostMessageRequest;
import passionmansour.teambeam.model.dto.chat.response.MessageListResponse;
import passionmansour.teambeam.model.dto.chat.response.MessageResponse;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.repository.MessageRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final JwtTokenService jwtTokenService;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageResponse createMessage(String token, PostMessageRequest postMessageRequest){
        Message message = Message.builder()
                .messageContent(postMessageRequest.getMessageContent())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .build();

        return new MessageResponse().form(messageRepository.save(message));
    }

    @Transactional
    public MessageResponse updateMessage(PatchMessageRequest patchMessageRequest){
        Message message = getById(patchMessageRequest.getMessageId());

        message.setMessageContent(patchMessageRequest.getMessageContent());
        message.setUpdateDate(LocalDateTime.now());

        return new MessageResponse().form(messageRepository.save(message));
    }

    @Transactional
    public void deleteMessage(Long messageId){
        Message message = getById(messageId);
        messageRepository.delete(message);
    }


    @Transactional(readOnly = true)
    public Message getById(Long messageId){
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        return message;
    }

    @Transactional(readOnly = true)
    public MessageListResponse getAllByProjectId(Long projectId){
        List<Message> messages = messageRepository.getByProjectId(projectId);
        return new MessageListResponse().entityToForm(messages);
    }
}
