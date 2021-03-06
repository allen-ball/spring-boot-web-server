package application.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "application", name = "credentials")
@Data @NoArgsConstructor
public class Credential {
    @Id @Column(length = 64, nullable = false, unique = true)
    @NotBlank @Email
    private String email = null;

    @Lob @Column(nullable = false)
    @NotBlank
    private String password = null;
}
