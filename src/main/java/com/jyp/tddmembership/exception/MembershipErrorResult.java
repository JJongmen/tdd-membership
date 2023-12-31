package com.jyp.tddmembership.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    UNKNOWN_EXCEPTION(HttpStatus.BAD_REQUEST, "Unknown Exception"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not Found"),
    NOT_MEMBERSHIP_OWNER(HttpStatus.FORBIDDEN, "Not Membership Owner"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
