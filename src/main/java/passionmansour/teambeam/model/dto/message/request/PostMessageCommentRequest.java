package passionmansour.teambeam.model.dto.message.request;

import lombok.Data;

@Data
public class PostMessageCommentRequest {
    private String messageCommentContent;
    private Long messageId;
}
