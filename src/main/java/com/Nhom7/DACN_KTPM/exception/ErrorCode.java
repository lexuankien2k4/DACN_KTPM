package com.Nhom7.DACN_KTPM.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1005, "Category not existed", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(1002, "Category not existed", HttpStatus.NOT_FOUND),
    CATEGORY_IN_USE(1009,"Category Cannot delete category ID",HttpStatus.BAD_REQUEST),
    CAR_MODEL_EXISTED(1002, "Car existed", HttpStatus.BAD_REQUEST),
    CAR_MODEL_NOT_FOUND(1005, "Car not existed", HttpStatus.NOT_FOUND),
    CAR_VARIANT_NOT_FOUND(1005, "Car variant not existed", HttpStatus.NOT_FOUND),
    CAR_VARIANT_EXISTED(1002, "Car variant existed", HttpStatus.BAD_REQUEST),
    BANK_EXISTED(1002, "Bank existed", HttpStatus.BAD_REQUEST),
    BANK_NOT_FOUND(1005, "Bank not existed", HttpStatus.NOT_FOUND),
    BANK_IN_USE(1009,"Bank Cannot delete category ID",HttpStatus.BAD_REQUEST),
    LOAN_POLICY_EXISTED(1002, "LoanPolicy existed", HttpStatus.BAD_REQUEST),
    LOAN_POLICY_NOT_FOUND(1005, "LoanPolicy not existed", HttpStatus.NOT_FOUND)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
