package application;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor @ToString @Log4j2
public class ControllerImpl {
    private static final String VIEW =
        ControllerImpl.class.getPackage().getName();

    private static final String FORM = "form";

    @Autowired private SpringTemplateEngine engine = null;
    @Autowired private SpringResourceTemplateResolver resolver = null;

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
}
