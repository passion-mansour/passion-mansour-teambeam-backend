package passionmansour.teambeam.model.dto.message.request;

import lombok.Data;

@Data
public class MessageCommentRequest {
    private String token;
    private String messageComment;
    private Long messageId;
    private Long projectId;
}
