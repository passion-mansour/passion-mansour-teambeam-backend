package passionmansour.teambeam.model.dto.member.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.StartPage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInformationResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long memberId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String memberName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mail;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String profileImage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StartPage startPage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private int notificationCount;

}
