package passionmansour.teambeam.model.dto.Memo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;
import passionmansour.teambeam.model.entity.Memo;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoResponse {
    private Long memoId;
    private String memoTile;
    private String memoContent;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private CreatorInfoResponse member;

    public MemoResponse form(Memo memo){
        return MemoResponse.builder()
                .memoId(memo.getMemoId())
                .memoTile(memo.getMemoTile())
                .memoContent(memo.getMemoContent())
                .createDate(memo.getCreateDate())
                .updateDate(memo.getUpdateDate())
                .member(new CreatorInfoResponse().form(memo.getMember()))
                .build();
    }
}
