package passionmansour.teambeam.model.dto.member.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import passionmansour.teambeam.model.enums.StartPage;

@Data
public class UpdateMemberRequest {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String profileImage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StartPage startPage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mail;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;

}
