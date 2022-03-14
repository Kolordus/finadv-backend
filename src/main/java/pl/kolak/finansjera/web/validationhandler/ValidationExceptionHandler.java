package pl.kolak.finansjera.web.validationhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionHandler {

    /*
        jeśli chcę sobie to ogarniac tylko dla jednego controllera to tylko tam umieszczam tę metodę
        jeśli chcę dla wszystkich to adnotacja jak powyżej
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream()
                .map(fieldError -> "Validation error: '" + fieldError.getDefaultMessage() + "' in '" +fieldError.getField() + "'")
                .collect(Collectors.toList());
    }
}
