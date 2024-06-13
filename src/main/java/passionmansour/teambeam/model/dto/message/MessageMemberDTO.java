package passionmansour.teambeam.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageMemberDTO implements Serializable {
    private Long memberId;
    private String memberName;
    private String profileImg;
}