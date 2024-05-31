package passionmansour.teambeam.model.dto.chat.request;

import lombok.Data;

@Data
public class PatchMessageCommentRequest {
    private Long messageCommentId;
    private String messageCommentContent;
    private Long messageId;
}
