package com.jyp.tddmembership.app.membership.repository;

import com.jyp.tddmembership.app.enums.MembershipType;
import com.jyp.tddmembership.app.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);
}
