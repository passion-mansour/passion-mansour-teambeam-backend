package passionmansour.teambeam.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.chat.request.PatchMessageRequest;
import passionmansour.teambeam.model.dto.chat.request.PostMessageCommentRequest;
import passionmansour.teambeam.model.dto.chat.response.MessageCommentListResponse;
import passionmansour.teambeam.model.dto.chat.response.MessageCommentResponse;
import passionmansour.teambeam.model.entity.MessageComment;
import passionmansour.teambeam.repository.MessageCommentRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageCommentService {
    private final JwtTokenService jwtTokenService;
    private final MessageService messageService;
    private final MessageCommentRepository messageCommentRepository;

    @Transactional
    public MessageCommentResponse createMessageComment(String token, PostMessageCommentRequest postMessageCommentRequest){
        MessageComment messageComment = MessageComment.builder()
                .messageCommentContent(postMessageCommentRequest.getMessageCommentContent())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .message(messageService.getById(postMessageCommentRequest.getMessageId()))
                .build();

        return new MessageCommentResponse().form(messageCommentRepository.save(messageComment));
    }

    @Transactional
    public MessageCommentResponse updateMessageComment(PatchMessageRequest patchMessageRequest){
        MessageComment messageComment = getById(patchMessageRequest.getMessageId());

        messageComment.setMessageCommentContent(patchMessageRequest.getMessageContent());
        messageComment.setUpdateDate(LocalDateTime.now());

        return new MessageCommentResponse().form(messageCommentRepository.save(messageComment));
    }

    @Transactional
    public void deleteMessageComment(Long messageId){
        MessageComment messageComment = getById(messageId);
        messageCommentRepository.delete(messageComment);
    }


    @Transactional(readOnly = true)
    public MessageComment getById(Long messageCommentId){
        MessageComment messageComment = messageCommentRepository.findById(messageCommentId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        return messageComment;
    }

    @Transactional(readOnly = true)
    public MessageCommentListResponse getAllByProjectId(Long messageId){
        List<MessageComment> messageComments = messageCommentRepository.getByMessageId(messageId);
        return new MessageCommentListResponse().entityToForm(messageComments);
    }
}
