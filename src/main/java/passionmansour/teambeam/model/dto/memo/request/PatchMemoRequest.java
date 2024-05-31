package passionmansour.teambeam.model.dto.memo.request;

import lombok.Data;

@Data
public class PatchMemoRequest {
    private Long memoId;
    private String memoTitle;
    private String memoContent;
}
