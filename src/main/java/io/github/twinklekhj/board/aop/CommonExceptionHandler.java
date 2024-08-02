package io.github.twinklekhj.board.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        // 404 에러 발생 시 로그 출력
        ex.printStackTrace();

        // 커스텀 응답 생성
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The resource was not found.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> parameterError(MethodArgumentNotValidException ex) {
        ex.printStackTrace();
        return ResponseEntity.badRequest()
                .body(ErrorResponse
                        .builder(ex.getCause(), HttpStatus.BAD_REQUEST, ex.getMessage()).build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> jsonParsingError(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("요청 파라미터가 누락되었습니다.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> commonError(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
