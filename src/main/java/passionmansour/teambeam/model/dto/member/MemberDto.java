package passionmansour.teambeam.model.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import passionmansour.teambeam.model.enums.StartPage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long memberId;
    private String mail;
    private String password;
    private String memberName;
    private int notificationCount;
    private String profileImage;
    private StartPage startPage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String refreshToken;

}
