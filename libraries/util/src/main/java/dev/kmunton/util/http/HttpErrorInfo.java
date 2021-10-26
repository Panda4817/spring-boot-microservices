package dev.kmunton.util.http;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class HttpErrorInfo {
    private Timestamp timestamp;
    private String path;
    private HttpStatus httpStatus;
    private String message;

    public HttpErrorInfo() {
    }

    public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.httpStatus = httpStatus;
        this.path = path;
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getError() {
        return httpStatus.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }
}
