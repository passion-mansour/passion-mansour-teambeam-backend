package passionmansour.teambeam.model.dto.message.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Message;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageListResponse {
    List<MessageResponse> messageResponses = new ArrayList<>();

    public MessageListResponse form(List<MessageResponse> messageResponses){
        this.setMessageResponses(messageResponses);
        return this;
    }

    public MessageListResponse entityToForm(List<Message> messages){
        List<MessageResponse> messageResponses = new ArrayList<>();

        if(messages != null) {
            for (Message message : messages) {
                messageResponses.add(new MessageResponse().form(message));
            }
        }

        this.setMessageResponses(messageResponses);
        return this;
    }
}
