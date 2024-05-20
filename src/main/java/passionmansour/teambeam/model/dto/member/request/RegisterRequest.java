package passionmansour.teambeam.model.dto.member.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Name cannot be null")
    private String memberName;

    @NotNull(message = "mail cannot be null")
    private String mail;

    @NotNull(message = "password cannot be null")
    private String password;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;

}