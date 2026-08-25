package com.nowhere.device_service.exception;

import com.nowhere.device_service.dto.ErrorDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleDeviceNotFound(DeviceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            {
                    DataIntegrityViolationException.class
            }
    )
    public ResponseEntity<ErrorDto> handleBadRequest() {
        return new ResponseEntity<>(
                new ErrorDto(HttpStatus.BAD_REQUEST.value(), "bad input data please try again with correct data", LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({SQLException.class, InvalidDataAccessResourceUsageException.class})
    public ResponseEntity<ErrorDto> handleSqlError(SQLException ex) {
        return new ResponseEntity<>(
                new ErrorDto(HttpStatus.SERVICE_UNAVAILABLE.value(), "internal services are down try again", LocalDateTime.now()),
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
                new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
