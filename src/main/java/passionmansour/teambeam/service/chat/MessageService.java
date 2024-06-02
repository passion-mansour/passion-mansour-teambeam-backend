package passionmansour.teambeam.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.chat.MessageDTO;
import passionmansour.teambeam.model.dto.chat.request.PatchMessageRequest;
import passionmansour.teambeam.model.dto.chat.request.PostMessageRequest;
import passionmansour.teambeam.model.dto.chat.response.MessageListResponse;
import passionmansour.teambeam.model.dto.chat.response.MessageResponse;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.MessageRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final JwtTokenService jwtTokenService;
    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Autowired
    public MessageService(JwtTokenService jwtTokenService, ProjectRepository projectRepository, RedisTemplate<String, Object> redisTemplate, ChannelTopic topic, MessageRepository messageRepository) {
        this.jwtTokenService = jwtTokenService;
        this.projectRepository = projectRepository;
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.messageRepository = messageRepository;
    }

    public List<Object> getMessages(String projectId) {
        List<Object> messages = redisTemplate.opsForList().range("chat:messages:" + projectId, 0, -1);
        if (messages == null || messages.isEmpty()) {
            messages = (List<Object>) (List<?>) messageRepository.findBy(projectId);
            messages.forEach(msg -> redisTemplate.opsForList().rightPush("chat:messages:" + projectId, msg));
        }
        return messages;
    }

    //메시지 저장하는 로직
    public Message saveMessage(Long projectId, MessageDTO messageDTO) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        Message message = Message.builder()
                .messageContent(messageDTO.getMessageContent())
                .member(jwtTokenService.getMemberByToken(messageDTO.getToken()))
                .project(optionalProject.get())
                .build();

        Message savedMessage = messageRepository.save(message);

        redisTemplate.opsForList().rightPush("chat:messages:" + projectId, savedMessage);
        redisTemplate.convertAndSend(topic.getTopic(), savedMessage);
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
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        List<Message> messages = messageRepository.findMessagesByProject(optionalProject.get());
        return new MessageListResponse().entityToForm(messages);
    }



    //    @Transactional
//    public MessageResponse updateMessage(PatchMessageRequest patchMessageRequest){
//        Message message = getById(patchMessageRequest.getMessageId());
//
//        message.setMessageContent(patchMessageRequest.getMessageContent());
//        message.setUpdateDate(LocalDateTime.now());
//
//        return new MessageResponse().form(messageRepository.save(message));
//    }
}
