package transfer.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleNotFound(IllegalArgumentException e) {
        log.warn("Encountered invalid argument exception, responding with [{}]: [{}]", HttpStatus.CONFLICT, e.getMessage(), e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(NotFoundException e) {
        log.warn("Encountered not found, responding with [{}]: [{}]", HttpStatus.NOT_FOUND, e.getMessage(), e);
    }


}
