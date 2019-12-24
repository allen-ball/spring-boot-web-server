package application;

import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = { "/clock/" })
@NoArgsConstructor @ToString @Log4j2
public class ClockController {
    private static final List<Locale> LOCALES =
        Stream.of(Locale.getAvailableLocales())
        .filter(t -> (! t.toString().equals("")))
        .collect(Collectors.toList());
    private static final List<TimeZone> ZONES =
        Stream.of(TimeZone.getAvailableIDs())
        .map(t -> TimeZone.getTimeZone(t))
        .collect(Collectors.toList());

    static {
        LOCALES.sort(Comparator.comparing(t -> t.toLanguageTag()));
        ZONES.sort(Comparator
                   .<TimeZone>comparingInt(t -> t.getRawOffset())
                   .thenComparingInt(t -> t.getDSTSavings()));
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session, Locale locale) {
        String languageTag = (String) session.getAttribute("languageTag");

        if (languageTag != null) {
            locale = Locale.forLanguageTag(languageTag);
        }

        String zoneID = (String) session.getAttribute("zoneID");
        TimeZone zone =
            (zoneID != null) ? TimeZone.getTimeZone(zoneID) : TimeZone.getDefault();

        model.addAttribute("locales", LOCALES);
        model.addAttribute("locale", locale);
        model.addAttribute("zones", ZONES);
        model.addAttribute("zone", zone);
        model.addAttribute("date", DateFormat.getDateInstance(DateFormat.LONG, locale));
        model.addAttribute("time", DateFormat.getTimeInstance(DateFormat.MEDIUM, locale));
        model.addAttribute("timestamp", new Date());

        model.asMap().values()
            .stream()
            .filter(t -> t instanceof DateFormat)
            .forEach(t -> ((DateFormat) t).setTimeZone(zone));
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "time" })
    public String get(HttpServletRequest request) {
        return request.getServletPath();
    }

    @RequestMapping(method = { RequestMethod.POST }, value = { "time" })
    public String post(HttpServletRequest request, HttpSession session,
                       @RequestParam("languageTag") String languageTag,
                       @RequestParam("zoneID") String zoneID) {
        session.setAttribute("languageTag", languageTag);
        session.setAttribute("zoneID", zoneID);

        return "redirect:" + request.getServletPath();
    }
}
