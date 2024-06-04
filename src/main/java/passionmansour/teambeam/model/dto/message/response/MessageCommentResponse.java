package passionmansour.teambeam.model.dto.message.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;
import passionmansour.teambeam.model.entity.MessageComment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCommentResponse {
    private Long messageCommentId;
    private String messageCommentContent;
    private String createDate;
    private String updateDate;
    private CreatorInfoResponse member;
    private Long messageId;

    public MessageCommentResponse form(MessageComment messageComment){
        return MessageCommentResponse.builder()
                .messageCommentId(messageComment.getMessageCommentId())
                .messageCommentContent(messageComment.getMessageCommentContent())
                .createDate(messageComment.getCreateDate())
                .updateDate(messageComment.getUpdateDate())
                .member(new CreatorInfoResponse().form(messageComment.getMember()))
                .messageId(messageComment.getMessage().getMessageId())
                .build();
    }
}
