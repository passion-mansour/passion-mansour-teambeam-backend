package passionmansour.teambeam.model.dto.message.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.MessageComment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long messageId;
    private String messageContent;
    private String createDate;
    private String updateDate;
    private CreatorInfoResponse member;
    private List<MessageCommentResponse> messageComments = new ArrayList<>();

    public MessageResponse form(Message message){
        List<MessageCommentResponse> messageCommentResponses = new ArrayList<>();

        for(MessageComment messageComment : message.getMessageComments()){
            messageCommentResponses.add(new MessageCommentResponse().form(messageComment));
        }

        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .messageContent(message.getMessageContent())
                .createDate(message.getCreateDate())
                .updateDate(message.getUpdateDate())
                .member(new CreatorInfoResponse().form(message.getMember()))
                .messageComments(messageComments)
                .build();
    }
}
