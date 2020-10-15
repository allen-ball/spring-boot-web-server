package application.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(catalog = "application", name = "credentials")
@NoArgsConstructor @EqualsAndHashCode
public class Credential {

    /** @serial */
    @Getter @Setter
    @Id @Column(length = 64, nullable = false, unique = true)
    @NotBlank @Email
    private String email = null;

    /** @serial */
    @Getter @Setter
    @Lob @Column(nullable = false)
    @NotBlank
    private String password = null;
}
