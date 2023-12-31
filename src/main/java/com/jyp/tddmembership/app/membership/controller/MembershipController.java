package com.jyp.tddmembership.app.membership.controller;

import com.jyp.tddmembership.app.membership.dto.MembershipAddResponse;
import com.jyp.tddmembership.app.membership.dto.MembershipDetailResponse;
import com.jyp.tddmembership.app.membership.dto.MembershipRequest;
import com.jyp.tddmembership.app.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jyp.tddmembership.app.membership.constants.MembershipConstants.USER_ID_HEADER;
import static com.jyp.tddmembership.app.membership.validation.ValidationGroups.MembershipAccumulateMarker;
import static com.jyp.tddmembership.app.membership.validation.ValidationGroups.MembershipAddMarker;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Validated(MembershipAddMarker.class) final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(
                userId,
                membershipRequest.getMembershipType(),
                membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipAddResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId) {

        return ResponseEntity.ok(membershipService.getMembership(membershipId, userId));
    }

    @DeleteMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId) {

        membershipService.removeMembership(membershipId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/memberships/{membershipId}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId,
            @RequestBody @Validated(MembershipAccumulateMarker.class) final MembershipRequest membershipRequest) {

        membershipService.accumulatePoint(membershipId, userId, membershipRequest.getPoint());
        return ResponseEntity.noContent().build();
    }
}
