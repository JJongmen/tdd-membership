package com.jyp.tddmembership.app.membership.service;

import com.jyp.tddmembership.app.enums.MembershipType;
import com.jyp.tddmembership.app.membership.dto.MembershipDetailResponse;
import com.jyp.tddmembership.app.membership.dto.MembershipAddResponse;
import com.jyp.tddmembership.app.membership.entity.Membership;
import com.jyp.tddmembership.app.membership.repository.MembershipRepository;
import com.jyp.tddmembership.app.point.service.PointService;
import com.jyp.tddmembership.exception.MembershipErrorResult;
import com.jyp.tddmembership.exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final PointService ratePointService;
    private final MembershipRepository membershipRepository;

    @Transactional
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
        if (!userId.equals(membership.getUserId())) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MembershipDetailResponse.builder()
                .id(membership.getId())
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .createdAt(membership.getCreatedAt())
                .build();
    }

    @Transactional
    public void removeMembership(final Long membershipId, final String userId) {
        final Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if (!userId.equals(membership.getUserId())) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        membershipRepository.deleteById(membershipId);
    }

    @Transactional
    public void accumulatePoint(final Long membershipId, final String userId, final int amount) {
        final Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if (!userId.equals(membership.getUserId())) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        final int additionalAmount = ratePointService.calculateAmount(amount);
        membership.setPoint(additionalAmount + membership.getPoint());
    }
}
