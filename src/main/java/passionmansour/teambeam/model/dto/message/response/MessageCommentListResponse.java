package passionmansour.teambeam.model.dto.message.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.MessageComment;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCommentListResponse {
    List<MessageCommentResponse> messageComments = new ArrayList<>();

    public MessageCommentListResponse form(List<MessageCommentResponse> messageCommentResponses){
        this.setMessageComments(messageCommentResponses);
        return this;
    }

    public MessageCommentListResponse entityToForm(List<MessageComment> messageComments){
        List<MessageCommentResponse> messageCommentResponses = new ArrayList<>();

        if(messageComments != null) {
            for (MessageComment messageComment : messageComments) {
                messageCommentResponses.add(new MessageCommentResponse().form(messageComment));
            }
        }

        this.setMessageComments(messageCommentResponses);
        return this;
    }
}

