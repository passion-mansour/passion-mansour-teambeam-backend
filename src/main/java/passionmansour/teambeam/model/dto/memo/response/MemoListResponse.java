package passionmansour.teambeam.model.dto.memo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Memo;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoListResponse {
    List<MemoResponse> memoResponses = new ArrayList<>();

    public MemoListResponse form(List<MemoResponse> memoResponses){
        this.setMemoResponses(memoResponses);
        return this;
    }

    public MemoListResponse entityToForm(List<Memo> memos){
        if(memos != null) {
            for (Memo memo : memos) {
                this.memoResponses.add(new MemoResponse().form(memo));
            }
        }

        return this;
    }
}
