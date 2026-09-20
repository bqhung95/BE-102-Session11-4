package com.example.patientmanage.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;              // HTTP status code (500, 404, 400...)
    private String error;            // Loại lỗi: "Internal Server Error"
    private String message;          // Thông báo ngắn gọn cho client
    private String path;             // Endpoint bị lỗi
    private LocalDateTime timestamp; // Thời điểm xảy ra
    private String traceId;          // Mã tra cứu — để đối chiếu với log server
}
