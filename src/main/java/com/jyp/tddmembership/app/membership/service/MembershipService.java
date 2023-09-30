package com.jyp.tddmembership.app.membership.service;

import com.jyp.tddmembership.app.enums.MembershipType;
import com.jyp.tddmembership.app.membership.dto.MembershipDetailResponse;
import com.jyp.tddmembership.app.membership.dto.MembershipAddResponse;
import com.jyp.tddmembership.app.membership.entity.Membership;
import com.jyp.tddmembership.app.membership.repository.MembershipRepository;
import com.jyp.tddmembership.exception.MembershipErrorResult;
import com.jyp.tddmembership.exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
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

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        final List<Membership> membershipList = membershipRepository.findAllByUserId(userId);

        return membershipList.stream()
                .map(m -> MembershipDetailResponse.builder()
                        .id(m.getId())
                        .membershipType(m.getMembershipType())
                        .point(m.getPoint())
                        .createdAt(m.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(final Long membershipId, final String userId) {
        final Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        return null;
    }
}
