package passionmansour.teambeam.model.dto.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCommentDTO implements Serializable {
    private Long messageCommentId;
    private String messageCommentContent;
    private String createDate;
    private String updateDate;
    private MessageMemberDTO member;
}
