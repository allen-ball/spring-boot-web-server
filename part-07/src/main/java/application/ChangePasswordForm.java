package application;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ChangePasswordForm {
    private String username;
    private String password;
    private String newPassword;
    private String repeatPassword;
}
