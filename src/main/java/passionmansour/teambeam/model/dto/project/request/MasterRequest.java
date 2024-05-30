package passionmansour.teambeam.model.dto.project.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterRequest {

    @Schema(description = "회원 고유 아이디")
    private Long memberId;

}
