package com.jyp.tddmembership;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.jyp.tddmembership.MembershipConstants.USER_ID_HEADER;

@RestController
public class MembershipController {

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody final MembershipRequest membershipRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
