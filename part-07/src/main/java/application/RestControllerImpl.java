package application;

import java.security.Principal;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public ResponseEntity<Object> whoAmI(Principal principal) throws Exception {
        return new ResponseEntity<>(principal, HttpStatus.OK);
    }

    @RequestMapping(method = { GET }, value = { "who" })
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Object> who() throws Exception {
        return new ResponseEntity<>(registry.getAllPrincipals(), HttpStatus.OK);
    }

    @ExceptionHandler({ SecurityException.class })
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Forbidden")
    public void handleFORBIDDEN() { }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ConflictException extends RuntimeException {
        private ConflictException(String message) { super(message, null); }
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public static class ForbiddenException extends RuntimeException {
        private ForbiddenException(String message) { super(message, null); }
    }

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public static class MethodNotAllowedException extends RuntimeException {
        private MethodNotAllowedException(String message) {
            super(message, null);
        }
    }
}
