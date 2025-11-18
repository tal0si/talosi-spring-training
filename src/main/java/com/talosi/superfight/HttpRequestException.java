package com.talosi.superfight;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpRequestException extends RuntimeException{

    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpRequestException(String message) {
        super(message);
    }
    public HttpRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
