package application;

import java.security.Principal;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = { "/api/" }, produces = APPLICATION_JSON_VALUE)
@NoArgsConstructor @ToString @Log4j2
public class RestControllerImpl {
    @Autowired private SessionRegistry registry = null;

    @RequestMapping(method = { GET }, value = { "who-am-i" })
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Principal> whoAmI(Principal principal) throws Exception {
        return new ResponseEntity<>(principal, HttpStatus.OK);
    }

    @RequestMapping(method = { GET }, value = { "who" })
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<List<Object>> who() throws Exception {
        return new ResponseEntity<>(registry.getAllPrincipals(), HttpStatus.OK);
    }

    @ExceptionHandler({ AccessDeniedException.class, SecurityException.class })
    public ResponseEntity<Object> handleFORBIDDEN() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
