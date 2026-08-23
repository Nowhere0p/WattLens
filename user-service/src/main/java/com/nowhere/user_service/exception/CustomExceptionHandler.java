package com.nowhere.user_service.exception;

import com.nowhere.user_service.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFound(UserNotFoundException ex){
        return  new ResponseEntity<>(
                new ErrorDto(HttpStatus.NOT_FOUND.value(),"user not found", LocalDateTime.now()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDto> handleSqlError(SQLException ex){
        return  new ResponseEntity<>(
                new ErrorDto(HttpStatus.SERVICE_UNAVAILABLE.value(),"internal services are down try again", LocalDateTime.now()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    @ExceptionHandler(
        {
            RuntimeException.class,
            InternalServerError.class,
        }
    )
    public ResponseEntity<Object> handleInternalServerError(RuntimeException ex) {
        return new ResponseEntity<>(
                new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage(),LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}



