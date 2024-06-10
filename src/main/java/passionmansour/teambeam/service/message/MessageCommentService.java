package passionmansour.teambeam.service.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.MessageComment;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.MessageCommentRepository;
import passionmansour.teambeam.repository.MessageRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MessageCommentService {
    private final JwtTokenService jwtTokenService;
    private final MessageCommentRepository messageCommentRepository;
    private final MessageRepository messageRepository;
    //private final SimpMessagingTemplate messagingTemplate;
    private final HashOperations<String, Long, MessageDTO> hashOperations;
    private final ConvertMessage convertMessage;


    @Autowired
    public MessageCommentService(JwtTokenService jwtTokenService, MessageCommentRepository messageCommentRepository, MessageRepository messageRepository, RedisTemplate<String, Object> redisTemplate, ConvertMessage convertMessage) {
        this.jwtTokenService = jwtTokenService;
        this.messageCommentRepository = messageCommentRepository;
        this.messageRepository = messageRepository;
        this.hashOperations = redisTemplate.opsForHash();
        this.convertMessage = convertMessage;
    }


    public MessageCommentDTO createComment(Long messageId, Long projectId, String commentContent, String token) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("Invalid message ID");
        }

        Message message = messageRepository.findById(messageId).get();
        Member member = jwtTokenService.getMemberByToken(token);

        MessageComment comment = new MessageComment();
        comment.setMessageCommentContent(commentContent);
        comment.setMessage(message);
        comment.setMember(member);
        //저장
        MessageCommentDTO messageComment = convertMessage.convertToMessageCommentDto(messageCommentRepository.save(comment));

        // 업데이트된 메시지 데이터 가져오기
        MessageDTO updatedMessage = convertMessage.convertToMessageDto(message);

        // Redis에 업데이트된 메시지 저장
        hashOperations.put("chat:messages:" + projectId, updatedMessage.getMessageId(), updatedMessage);

        return messageComment;
    }


    @Transactional
    public List<MessageCommentDTO> getAllComment(Long messageId){
        List<MessageCommentDTO> messageComment = messageCommentRepository.findByMessage_MessageId(messageId)
                .stream()
                .map(convertMessage::convertToMessageCommentDto)
                .toList();

        return messageComment;
    }

/*    private void updateCommentCountInRedis(Long messageId, Long projectId) {
        // Redis에서 메시지 가져오기
        MessageDTO messageDTO = hashOperations.get("chat:messages:" + projectId, messageId);

        if (messageDTO != null) {
            // 댓글 수 증가
            messageDTO.setCommentCount(messageDTO.getCommentCount() + 1);

            // Redis에 저장 (해시 업데이트)
            hashOperations.put("chat:messages:" + projectId, messageId, messageDTO);
        }
    }

    public int getCommentCountByMessageId(Long messageId) {
        return messageCommentRepository.countMessageCommentsByMessage_MessageId(messageId);
    }*/
}
