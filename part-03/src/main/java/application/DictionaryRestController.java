package application;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/dictionary/" },
                produces = MediaType.APPLICATION_JSON_VALUE)
@NoArgsConstructor @ToString @Log4j2
public class DictionaryRestController {
    @Autowired private Map<String,String> dictionary = null;

    @RequestMapping(method = { RequestMethod.GET }, value = { "get" })
    public Optional<String> get(@RequestParam Map<String,String> parameters) {
        if (parameters.size() != 1) {
            throw new IllegalArgumentException();
        }

        Map.Entry<String,String> entry =
            parameters.entrySet().iterator().next();
        String result = dictionary.get(entry.getKey());

        return Optional.ofNullable(result);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "put" })
    public Optional<String> put(@RequestParam Map<String,String> parameters) {
        if (parameters.size() != 1) {
            throw new IllegalArgumentException();
        }

        Map.Entry<String,String> entry =
            parameters.entrySet().iterator().next();
        String result = dictionary.put(entry.getKey(), entry.getValue());

        return Optional.ofNullable(result);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "remove" })
    public Optional<String> remove(@RequestParam Map<String,String> parameters) {
        if (parameters.size() != 1) {
            throw new IllegalArgumentException();
        }

        Map.Entry<String,String> entry =
            parameters.entrySet().iterator().next();
        String result = dictionary.remove(entry.getKey());

        return Optional.ofNullable(result);
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "size" })
    public int size(@RequestParam Map<String,String> parameters) {
        if (! parameters.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return dictionary.size();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "entrySet" })
    public Set<Map.Entry<String,String>> entrySet(@RequestParam Map<String,String> parameters) {
        if (! parameters.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return dictionary.entrySet();
    }

    @RequestMapping(method = { RequestMethod.GET }, value = { "keySet" })
    public Set<String> keySet(@RequestParam Map<String,String> parameters) {
        if (! parameters.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return dictionary.keySet();
    }
}
