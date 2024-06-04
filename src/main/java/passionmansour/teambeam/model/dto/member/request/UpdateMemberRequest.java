package passionmansour.teambeam.model.dto.member.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import passionmansour.teambeam.model.enums.StartPage;

@Data
public class UpdateMemberRequest {

    @Schema(description = "프로필 이미지")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String profileImage;
    @Schema(description = "시작 페이지")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private StartPage startPage;
    @Schema(description = "메일")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String mail;
    @Schema(description = "이름")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String memberName;

}
