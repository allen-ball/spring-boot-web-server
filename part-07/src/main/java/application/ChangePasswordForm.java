package application;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = false) @ToString @Log4j2
public class ChangePasswordForm {
    private String username;
    private String password;
    private String newPassword;
    private String repeatPassword;
}
