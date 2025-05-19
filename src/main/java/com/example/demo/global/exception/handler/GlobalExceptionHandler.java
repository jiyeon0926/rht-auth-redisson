package com.example.demo.global.exception.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<String> handleMethodValidationExceptions(HandlerMethodValidationException e) {
        String message = e.getParameterValidationResults()
                .getFirst()
                .getResolvableErrors()
                .getFirst()
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .getFirst()
                .getDefaultMessage();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<String> handleResponseStatusExceptions(ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getReason());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<String> handleAuthException(AuthenticationException e) {
        HttpStatus statusCode = e instanceof BadCredentialsException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(statusCode)
                .body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<String> handleJwtException(JwtException e) {
        HttpStatus httpStatus;

        if (e instanceof ExpiredJwtException) { // JWT 토큰 만료
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof MalformedJwtException) { // 잘못된 JWT 토큰
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity
                .status(httpStatus)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentExceptions(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleOtherExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
