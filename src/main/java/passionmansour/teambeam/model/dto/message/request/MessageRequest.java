package passionmansour.teambeam.model.dto.message.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String token;
    private String messageContent;
    private Long projectId;
}
