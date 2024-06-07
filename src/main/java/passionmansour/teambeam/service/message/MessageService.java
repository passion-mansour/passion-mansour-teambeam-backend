package passionmansour.teambeam.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.MessageMemberDTO;
import passionmansour.teambeam.model.dto.message.request.MessageRequest;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.MessageRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final JwtTokenService jwtTokenService;
    private final MessageRepository messageRepository;
    private final ProjectRepository projectRepository;
    private final HashOperations<String, Long, MessageDTO> hashOperations;
    private final MessageCommentService messageCommentService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ConvertMessage convertMessage;

    @Autowired
    public MessageService(JwtTokenService jwtTokenService, ProjectRepository projectRepository, RedisTemplate<String, Object> redisTemplate, MessageRepository messageRepository, MessageCommentService messageCommentService, RedisTemplate<String, String> redisTemplate1, ConvertMessage convertMessage) {
        this.jwtTokenService = jwtTokenService;
        this.projectRepository = projectRepository;
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.messageCommentService = messageCommentService;
        this.convertMessage = convertMessage;
    }

    public List<MessageDTO> getMessages(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findByProjectId(projectId);
        if (optionalProject.isEmpty()) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        // Redis 해시에서 모든 메시지 가져오기
        List<MessageDTO> messages = hashOperations.values("chat:messages:" + projectId)
                .stream().map(obj -> (MessageDTO) obj).collect(Collectors.toList());

        // Redis에 메시지가 없으면 MySQL에서 가져오고 Redis에 저장
        if (messages.isEmpty()) {
            List<Message> messageEntities = messageRepository.findMessagesByProject(optionalProject.get());
            messages = messageEntities.stream()
                    .map(convertMessage::convertToMessageDto)
                    .collect(Collectors.toList());
            messages.forEach(msg -> hashOperations.put("chat:messages:" + projectId, msg.getMessageId(), msg));
        }

        messages.sort(Comparator.comparing(MessageDTO::getMessageId));

        return messages;
    }

    //메시지 저장하는 로직
    @Transactional
    public MessageDTO createMessage(Long projectId, MessageRequest messageRequest) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            throw new IllegalArgumentException("Invalid project ID");
        }

        Message message = new Message();
        message.setMessageContent(messageRequest.getMessageContent());
        message.setProject(optionalProject.get());
        message.setMember(jwtTokenService.getMemberByToken(messageRequest.getToken()));

        //mysql에 저장
        Message savedMessage = messageRepository.save(message);

        //DTO 전환
        MessageDTO messageDTO = convertMessage.convertToMessageDto(savedMessage);

        //redis에 저장
        hashOperations.put("chat:messages:" + projectId, messageDTO.getMessageId(), messageDTO);


        return messageDTO;
    }



    @Transactional
    public void deleteMessage(Long messageId){
        Optional<Message> message = messageRepository.findById(messageId);
        messageRepository.delete(message.get());
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
