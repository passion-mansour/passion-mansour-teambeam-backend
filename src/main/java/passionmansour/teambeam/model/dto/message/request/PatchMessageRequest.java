package passionmansour.teambeam.model.dto.message.request;

import lombok.Data;

@Data
public class PatchMessageRequest {
    private Long messageId;
    private String messageContent;
}
