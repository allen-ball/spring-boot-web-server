package application;

import application.jpa.Credential;
import application.jpa.CredentialRepository;
import java.util.HashSet;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor @ToString @Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired private CredentialRepository credentialRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        try {
log.info("{}", username);
            Optional<Credential> credential =
                credentialRepository.findById(username);
log.info("{}", credential);
            HashSet<GrantedAuthority> set = new HashSet<>();
/*
            subscriberRepository.findById(username)
                .ifPresent(t -> set.add(new SimpleGrantedAuthority("SUBSCRIBER")));

            authorRepository.findById(username)
                .ifPresent(t -> set.add(new SimpleGrantedAuthority("AUTHOR")));
*/
            user = new User(username, credential.get().getPassword(), set);
        } catch (UsernameNotFoundException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
