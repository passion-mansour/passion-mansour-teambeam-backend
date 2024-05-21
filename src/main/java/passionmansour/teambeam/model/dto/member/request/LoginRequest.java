package passionmansour.teambeam.model.dto.member.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "mail cannot be null")
    private String mail;

    @NotNull(message = "password cannot be null")
    private String password;
}