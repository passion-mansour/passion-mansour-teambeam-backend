package passionmansour.teambeam.model.dto.member;

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
    private StartPage startPage;
    private String accessToken;
    private String refreshToken;

}
