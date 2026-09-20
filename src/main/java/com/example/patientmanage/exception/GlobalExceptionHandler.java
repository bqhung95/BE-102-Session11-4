package com.example.patientmanage.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.UUID;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================================
    // 1. XỬ LÝ LỖI NGHIỆP VỤ (400) — KHÔNG cần log stack trace
    // ==========================================================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        // ✅ Log WARN — không cần stack trace vì đây là lỗi "biết trước"
        log.warn("Business error tại [{}]: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // ==========================================================
    // 2. XỬ LÝ KHÔNG TÌM THẤY (404) — log WARN
    // ==========================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found tại [{}]: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // ==========================================================
    // 3. VALIDATION (400) — log WARN + danh sách field lỗi
    // ==========================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dữ liệu không hợp lệ");

        log.warn("Validation failed tại [{}]: {}", request.getRequestURI(), message);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    // ==========================================================
    // ⭐ 4. XỬ LÝ LỖI HỆ THỐNG CHUNG (500)
    //    ĐÂY LÀ PHẦN CHÍNH CỦA BÀI TẬP
    // ==========================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception e, HttpServletRequest request) {

        // ✅ Sinh traceId để đối chiếu giữa Client và Server log
        String traceId = UUID.randomUUID().toString();

        // ✅ LOG ERROR VỚI ĐẦY ĐỦ STACK TRACE
        //    - Tham số thứ 2 (e) chính là Throwable → Logback in full stack trace
        //    - traceId giúp tra cứu nhanh trong file log
        log.error("Lỗi hệ thống xảy ra [traceId={}, path={}, method={}]: ",
                traceId,
                request.getRequestURI(),
                request.getMethod(),
                e);   // 👈 Stack trace được in tự động nhờ truyền `e`

        // ✅ KHÔNG trả stack trace về client — chỉ trả thông báo chung chung
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Hệ thống đang gặp sự cố. Vui lòng liên hệ hỗ trợ với mã: " + traceId)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
