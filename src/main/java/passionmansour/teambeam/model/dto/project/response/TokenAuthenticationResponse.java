package passionmansour.teambeam.model.dto.project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class TokenAuthenticationResponse {

    private String message;
    private boolean isMember;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;
}
