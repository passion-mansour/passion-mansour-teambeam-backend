package passionmansour.teambeam.model.dto.chat.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageResponse {
    private Long messageId;
    private String messageContent;
    private Date createDate;
    private LocalDateTime updateDate;
    private CreatorInfoResponse member;
    private List<MessageCommentResponse> messageComments = new ArrayList<>();

    public MessageResponse messageResponse;
}
