package passionmansour.teambeam.model.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageResponse {
    private String imageName;
    private String base64;
}
