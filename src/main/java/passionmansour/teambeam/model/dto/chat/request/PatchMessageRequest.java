package passionmansour.teambeam.model.dto.chat.request;

import lombok.Data;

@Data
public class PatchMessageRequest {
    private Long messageId;
    private String messageContent;
}
