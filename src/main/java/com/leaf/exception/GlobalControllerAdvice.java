package com.leaf.exception;

import com.leaf.vo.LeafResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<LeafResponse> leafException(RuntimeException exception) {

        LeafResponse<String> result = LeafResponse.<String>builder().result(exception.getMessage()).build();
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<LeafResponse> leafException(GlobalException exception) {

        LeafResponse<String> result = LeafResponse.<String>builder().result(exception.getMessage()).build();
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GlobalServerException.class)
    public ResponseEntity<LeafResponse> leafException(GlobalServerException exception) {
        LeafResponse<String> result = LeafResponse.<String>builder().result(exception.getMessage()).build();
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
