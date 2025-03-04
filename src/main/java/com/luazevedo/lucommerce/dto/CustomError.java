package com.luazevedo.lucommerce.dto;

import java.time.Instant;

public class CustomError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    /*
    É o mesmo retorno do Json veja:
    {
    "timestamp": "2025-03-03T21:40:25.910+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/products/100"
     */


    public CustomError(Instant timestamp, Integer status, String error, String path ) {
        this.error = error;
        this.path = path;
        this.status = status;
        this.timestamp = timestamp;
    }



    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public Integer getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
