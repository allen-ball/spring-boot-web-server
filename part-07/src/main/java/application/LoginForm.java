package application;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class LoginForm {
    private String username;
    private String password;
}
