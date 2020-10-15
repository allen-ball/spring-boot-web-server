package application.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link Credential} {@link JpaRepository}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision: 5285 $
 */
@Repository
@Transactional(readOnly = true)
public interface CredentialRepository extends JpaRepository<Credential,String> {
}
