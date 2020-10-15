package application;

import java.security.Principal;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = { "/api/" }, produces = APPLICATION_JSON_VALUE)
@NoArgsConstructor @ToString @Log4j2
public class RestControllerImpl {
    @RequestMapping(method = { GET }, value = { "/who" })
    public ResponseEntity<String> who(Principal principal) throws Exception {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
