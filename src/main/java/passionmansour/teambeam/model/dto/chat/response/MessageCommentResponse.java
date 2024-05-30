package passionmansour.teambeam.model.dto.chat.response;

import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;

import java.time.LocalDateTime;

public class MessageCommentResponse {
    private Long messageCommentId;
    private String messageCommentContent;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private CreatorInfoResponse member;
    private Long messageId;
}
