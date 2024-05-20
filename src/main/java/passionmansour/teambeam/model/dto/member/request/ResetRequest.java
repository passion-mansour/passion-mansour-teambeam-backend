package passionmansour.teambeam.model.dto.member.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetRequest {

    @NotNull(message = "token cannot be null")
    private String token;

    @NotNull(message = "newPassword cannot be null")
    private String newPassword;

}
