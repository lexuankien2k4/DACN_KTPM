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
    LOAN_POLICY_NOT_FOUND(1005, "LoanPolicy not existed", HttpStatus.NOT_FOUND),
    INVALID_CALCULATION_INPUT(1010,"Need to provide Policy ID or InterestRate",HttpStatus.BAD_REQUEST),
    INVALID_INPUT(1010,"loi inval",HttpStatus.BAD_REQUEST),
    VARIANT_NOT_FOUND(1005, "variant not existed", HttpStatus.NOT_FOUND),
    SHOWROOM_NOT_FOUND(1005, "showroom not existed", HttpStatus.NOT_FOUND),
    REQUEST_NOT_FOUND(1005, "Request not existed", HttpStatus.NOT_FOUND),
    SHOWROOM_REQUIRED_FOR_SCHEDULE(1002, "error ", HttpStatus.BAD_REQUEST),
    SCHEDULE_SLOT_TAKEN(1011,"full slot" , HttpStatus.BAD_REQUEST),
    // ... các enum cũ
    // Sửa dòng này: Thêm HttpStatus.INTERNAL_SERVER_ERROR (Lỗi server)
    PAYMENT_CREATION_FAILED(2001, "Failed to create payment URL", HttpStatus.INTERNAL_SERVER_ERROR),

    // Sửa dòng này: Thêm HttpStatus.BAD_REQUEST (Lỗi dữ liệu đầu vào)
    INVALID_AMOUNT(2002, "Amount must be greater than 0", HttpStatus.BAD_REQUEST),
    // ...
    JSON_PROCESSING_ERROR(1011,"mistake processing json" ,HttpStatus.BAD_REQUEST );


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
