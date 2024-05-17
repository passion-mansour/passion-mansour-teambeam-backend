package passionmansour.teambeam.model.dto.member;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "mail cannot be null")
    private String mail;

    @NotNull(message = "password cannot be null")
    private String password;
}