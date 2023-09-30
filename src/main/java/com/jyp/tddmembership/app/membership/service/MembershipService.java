package com.jyp.tddmembership.app.membership.service;

import com.jyp.tddmembership.app.enums.MembershipType;
import com.jyp.tddmembership.app.membership.dto.MembershipResponse;
import com.jyp.tddmembership.app.membership.entity.Membership;
import com.jyp.tddmembership.app.membership.repository.MembershipRepository;
import com.jyp.tddmembership.exception.MembershipErrorResult;
import com.jyp.tddmembership.exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        final Membership savedMembership = membershipRepository.save(membership);

        return MembershipResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }
}
