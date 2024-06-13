package passionmansour.teambeam.model.dto.message.request;

import lombok.Data;

@Data
public class PatchMessageCommentRequest {
    private Long messageCommentId;
    private String messageCommentContent;
    private Long messageId;
}
