package passionmansour.teambeam.model.dto.chat.request;

import lombok.Data;

@Data
public class PostMessageCommentRequest {
    private String messageCommentContent;
    private Long messageId;
}
