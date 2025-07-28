package com.zizonhyunwoo.anysearch.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalException {

    //   요청 body 에 대한 exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Objects.requireNonNull(error.getDefaultMessage()).startsWith("Failed")
                                ? "잘못된 형식입니다." : error.getDefaultMessage()
                ));
        log.info(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 타입 문제 처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationExceptions(HttpMessageNotReadableException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    // 찾지 못한 나머지 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> generalException(Exception e){
        log.info(e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    // 검색 관련 예외 처리
    @ExceptionHandler(SearchException.class)
    public ResponseEntity<String> handleSearchException(SearchException e){
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 경로에서 지원하지 않는 예외 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

}
