package passionmansour.teambeam.model.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    private Long messageId;
    private String messageContent;
    private String createDate;
    private String updateDate;
    private MessageMemberDTO member;
    private Long projectId;
    private int commentCount;
    private List<MessageCommentDTO> messageComments;
}
