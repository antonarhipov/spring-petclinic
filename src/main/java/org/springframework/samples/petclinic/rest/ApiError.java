package org.springframework.samples.petclinic.rest;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Standard error format for API responses
 */
@JsonInclude(Include.NON_NULL)
public class ApiError {

    private String code;
    private String status;
    private String message;
    private List<String> details;

    // Default constructor
    public ApiError() {
        this.details = new ArrayList<>();
    }

    // Constructor with code, status, and message
    public ApiError(String code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.details = new ArrayList<>();
    }

    // Add a detail to the error
    public void addDetail(String detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        this.details.add(detail);
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
