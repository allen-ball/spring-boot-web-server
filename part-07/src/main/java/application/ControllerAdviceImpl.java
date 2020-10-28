package application;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@NoArgsConstructor @ToString @Log4j2
public class ControllerAdviceImpl {
    @Autowired private ApplicationContext context = null;
    private List<ClientRegistration> oauth2 = null;

    @PostConstruct
    public void init() { }

    @PreDestroy
    public void destroy() { }

    @ModelAttribute("oauth2")
    public List<ClientRegistration> oauth2() {
        if (oauth2 == null) {
            oauth2 = new ArrayList<>();

            try {
                ClientRegistrationRepository repository =
                    context.getBean(ClientRegistrationRepository.class);

                if (repository != null) {
                    ResolvableType type =
                        ResolvableType.forInstance(repository)
                        .as(Iterable.class);

                    if (type != ResolvableType.NONE
                        && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
                        ((Iterable<?>) repository)
                            .forEach(t -> oauth2.add((ClientRegistration) t));
                    }
                }
            } catch (Exception exception) {
            }
        }

        return oauth2;
    }

    @ModelAttribute("hasPassword")
    public boolean hasPassword(Principal principal) {
        return principal instanceof UsernamePasswordAuthenticationToken;
    }
}
