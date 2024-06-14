package passionmansour.teambeam.model.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatorInfoResponse{
    private Long memberId;
    private String memberName;
    private String profileImage;

    public CreatorInfoResponse form(Member member){
        return CreatorInfoResponse.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .profileImage(member.getProfileImage())
                .build();
    }

    // 프로필 이미지 인코딩값 반환용
    public CreatorInfoResponse form(Member member, String encodedProfileImage){
        return CreatorInfoResponse.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .profileImage(encodedProfileImage)
            .build();
    }
}
