package passionmansour.teambeam.model.dto.Memo.request;

import lombok.Data;

@Data
public class PatchMemoRequest {
    private Long memoId;
    private String memoTitle;
    private String memoContent;
}
