package application;

import java.text.Collator;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

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

    @RequestMapping(method = { RequestMethod.GET }, value = { "time" })
    public void get(Model model, Locale locale, TimeZone zone,
                    @SessionAttribute Optional<String> languageTag,
                    @SessionAttribute Optional<String> zoneID) {
        if (languageTag.isPresent()) {
            locale = Locale.forLanguageTag(languageTag.get());
        }

        if (zoneID.isPresent()) {
            zone = TimeZone.getTimeZone(zoneID.get());
        }

        model.addAttribute("locale", locale);
        model.addAttribute("zone", zone);

        DateFormat date = DateFormat.getDateInstance(DateFormat.LONG, locale);
        DateFormat time = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);

        model.addAttribute("date", date);
        model.addAttribute("time", time);

        for (Object object : model.asMap().values()) {
            if (object instanceof DateFormat) {
                ((DateFormat) object).setTimeZone(zone);
            }
        }

        Collator collator = Collator.getInstance(locale);
        List<Locale> locales =
            LOCALES.stream()
            .sorted(Comparator.comparing(Locale::toLanguageTag, collator))
            .collect(Collectors.toList());
        List<TimeZone> zones =
            ZONES.stream()
            .sorted(Comparator
                    .comparingInt(TimeZone::getRawOffset)
                    .thenComparingInt(TimeZone::getDSTSavings)
                    .thenComparing(TimeZone::getID, collator))
            .collect(Collectors.toList());

        model.addAttribute("locales", locales);
        model.addAttribute("zones", zones);

        Date timestamp = new Date();

        model.addAttribute("timestamp", timestamp);
    }

    @RequestMapping(method = { RequestMethod.POST }, value = { "time" })
    public String post(HttpServletRequest request, HttpSession session,
                       @RequestParam Map<String,String> form) {
        for (Map.Entry<String,String> entry : form.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }

        return "redirect:" + request.getServletPath();
    }
}
