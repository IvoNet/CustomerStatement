package nl.ordina.rabobankcustomerstatement.exception;

import nl.ordina.rabobankcustomerstatement.model.ResponseStatement;
import nl.ordina.rabobankcustomerstatement.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStatement> handle(final Exception ex,
                                                    final HttpServletRequest request,
                                                    final HttpServletResponse response) {
        final var ret = new ResponseStatement();
        if (ex instanceof NullPointerException) {
            ret.result(Result.BAD_REQUEST);
            return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
        }
        ret.result(Result.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
