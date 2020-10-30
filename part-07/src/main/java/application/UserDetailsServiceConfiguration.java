package application;

import application.jpa.Authority;
import application.jpa.AuthorityRepository;
import application.jpa.Credential;
import application.jpa.CredentialRepository;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@NoArgsConstructor @ToString @Log4j2
public class UserDetailsServiceConfiguration {
    @Autowired private CredentialRepository credentialRepository = null;
    @Autowired private AuthorityRepository authorityRepository = null;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @NoArgsConstructor @ToString
    private class UserDetailsServiceImpl implements UserDetailsService {
        @Override
        @Transactional(readOnly = true)
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = null;

            try {
                Optional<Credential> credential = credentialRepository.findById(username);
                Optional<Authority> authority = authorityRepository.findById(username);

                user =
                    new User(username,
                             credential.get().getPassword(),
                             authority.map(t -> t.getGrants().asGrantedAuthorityList())
                             .orElse(AuthorityUtils.createAuthorityList()));
            } catch (UsernameNotFoundException exception) {
                throw exception;
            } catch (Exception exception) {
                throw new UsernameNotFoundException(username);
            }

            return user;
        }
    }
}
