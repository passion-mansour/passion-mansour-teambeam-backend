package passionmansour.teambeam.model.dto.member.request;

import lombok.Data;

@Data
public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;

}
