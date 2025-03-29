package com.petlog.common.exception;

import com.petlog.userService.dto.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BizRuntimeException.class})
    @ResponseBody
    public ResponseEntity<ResponseMessage> handleBizRuntimeException(BizRuntimeException e) {
        log.error("BizRuntime Error: " + e.getErrorMessage(), e);

        int statusCode = Integer.parseInt(e.getErrorCode());
        HttpStatus httpStatus = HttpStatus.resolve(statusCode);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .statusCode(Integer.parseInt(e.getErrorCode()))
                .resultMessage(e.getErrorMessage())
                .detailMessage(e.getMessage())
                .build();

        return new ResponseEntity<>(responseMessage, httpStatus);
    }

    // 위에서 처리되지 않은 모든 예외를 여기서 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ResponseMessage> handleGeneralException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Unexpected Error: " + e.getMessage(), e);

        String msgId = ""; // 에러 코드
        String msg = e.getMessage(); // 에러 메세지

        if (e instanceof BizRuntimeException) {
            BizRuntimeException base = (BizRuntimeException) e;
            msgId = base.getErrorCode();
            msg = base.getMessage();
        }

        ResponseMessage responseMessage = ResponseMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .resultMessage("서버 내부 오류가 발생했습니다.") // 사용자에게 보일 메세지
                .detailMessage(msg) // 디버깅용 메세지
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
    }
}