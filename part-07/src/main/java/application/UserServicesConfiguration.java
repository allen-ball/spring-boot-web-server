package application;

import application.jpa.AuthorityRepository;
import application.jpa.Credential;
import application.jpa.CredentialRepository;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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
    public OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService() {
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
                Credential credential =
                    credentialRepository.findById(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));
                List<GrantedAuthority> authorities =
                    authorityRepository.findById(username)
                    .map(t -> t.getGrants().asGrantedAuthorityList())
                    .orElse(AuthorityUtils.createAuthorityList());

                user = new User(username, credential.getPassword(), authorities);
            } catch (UsernameNotFoundException exception) {
                throw exception;
            } catch (Exception exception) {
                throw new UsernameNotFoundException(username);
            }

            return user;
        }
    }

    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES =
        AuthorityUtils.createAuthorityList(Authorities.USER.name());

    @NoArgsConstructor @ToString
    private class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
        private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        @Override
        @Transactional(readOnly = true)
        public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
            String attribute =
                request.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
            OAuth2User user = delegate.loadUser(request);
            List<GrantedAuthority> authorities =
                authorityRepository.findById(user.getName())
                .map(t -> t.getGrants().asGrantedAuthorityList())
                .orElse(DEFAULT_AUTHORITIES);

            return new DefaultOAuth2User(authorities, user.getAttributes(), attribute);
        }
    }

    @NoArgsConstructor @ToString
    private class OidcUserServiceImpl extends OidcUserService {
        { setOauth2UserService(oAuth2UserService()); }

        @Override
        @Transactional(readOnly = true)
        public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
            String attribute =
                request.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
            OidcUser user = super.loadUser(request);
            List<GrantedAuthority> authorities =
                authorityRepository.findById(user.getName())
                .map(t -> t.getGrants().asGrantedAuthorityList())
                .orElse(DEFAULT_AUTHORITIES);

            return new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo(), attribute);
        }
    }
}
