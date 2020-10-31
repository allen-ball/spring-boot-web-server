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
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@NoArgsConstructor @ToString @Log4j2
public class UserServicesConfiguration {
    @Autowired private CredentialRepository credentialRepository = null;
    @Autowired private AuthorityRepository authorityRepository = null;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest,OAuth2User> oauth2UserService() {
        return new OAuth2UserServiceImpl();
    }

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserServiceImpl();
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

    @NoArgsConstructor @ToString
    private class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
        private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        @Override
        public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
            OAuth2User user = delegate.loadUser(request);

            log.info("{}", user);

            return user;
        }
    }

    @NoArgsConstructor @ToString
    private class OidcUserServiceImpl extends OidcUserService {
        { setOauth2UserService(oauth2UserService()); }
    }
}
