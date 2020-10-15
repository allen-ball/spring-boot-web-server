package application.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "application", name = "authorities")
@Data @NoArgsConstructor
public class Authority {
    @Id @Column(length = 64, nullable = false, unique = true)
    @NotBlank @Email
    private String email = null;

    @Column(nullable = false)
    private AuthoritiesSet grants = new AuthoritiesSet();
}
