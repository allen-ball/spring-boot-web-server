package application;

import org.springframework.security.core.session.SessionRegistry;
import java.security.Principal;
import application.jpa.Credential;
import application.jpa.CredentialRepository;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor @ToString @Log4j2
public class ControllerImpl {
    private static final String VIEW =
        ControllerImpl.class.getPackage().getName();

    private static final String EXCEPTION = "exception";
    private static final String FORM = "form";
    private static final String PRINCIPAL = "principal";
    private static final String PRINCIPALS = "principals";

    @Autowired private SpringTemplateEngine engine = null;
    @Autowired private SpringResourceTemplateResolver resolver = null;
    @Autowired private CredentialRepository credentialRepository = null;
    @Autowired private PasswordEncoder encoder = null;
    @Autowired private SessionRegistry registry = null;

    @PostConstruct
    public void init() { resolver.setUseDecoupledLogic(true); }

    @PreDestroy
    public void destroy() { }

    @RequestMapping(value = { "/" })
    public String root() {
        return VIEW;
    }

    @RequestMapping(method = { GET }, value = { "/login" })
    public String login(Model model, HttpSession session) {
        model.addAttribute(FORM, new LoginForm());

        return VIEW;
    }

    @RequestMapping(method = { GET }, value = { "/password" })
    @PreAuthorize("isAuthenticated()")
    public String password(Model model) {
        model.addAttribute(FORM, new ChangePasswordForm());

        return VIEW;
    }

    @RequestMapping(method = { POST }, value = { "/password" })
    @PreAuthorize("isAuthenticated()")
    public String passwordPOST(Model model,
                               @Valid ChangePasswordForm form,
                               BindingResult result) {
        try {
            if (result.hasErrors()) {
                throw new RuntimeException(String.valueOf(result.getAllErrors()));
            }

            if (! (form.getNewPassword() != null
                   && form.getNewPassword().equals(form.getRepeatPassword()))) {
                throw new RuntimeException("Repeated password does not match new password");
            }

            Credential credential =
                credentialRepository.findById(form.getUsername()).get();

            if (! encoder.matches(form.getPassword(), credential.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            credential.setPassword(encoder.encode(form.getNewPassword()));

            credentialRepository.save(credential);
        } catch (Exception exception) {
            model.addAttribute(FORM, form);
            model.addAttribute(EXCEPTION, exception);
        }

        return VIEW;
    }

    @RequestMapping(value = { "/who-am-i" })
    @PreAuthorize("hasAuthority('USER')")
    public String whoAmI(Model model, Principal principal) {
        model.addAttribute(PRINCIPAL, principal);

        return VIEW;
    }

    @RequestMapping(value = { "/who" })
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public String who(Model model) {
        model.addAttribute(PRINCIPALS, registry.getAllPrincipals());

        return VIEW;
    }
}
