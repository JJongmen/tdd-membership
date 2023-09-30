package com.jyp.tddmembership.app.membership.service;

import com.jyp.tddmembership.app.enums.MembershipType;
import com.jyp.tddmembership.app.membership.dto.MembershipResponse;
import com.jyp.tddmembership.app.membership.entity.Membership;
import com.jyp.tddmembership.app.membership.repository.MembershipRepository;
import com.jyp.tddmembership.exception.MembershipErrorResult;
import com.jyp.tddmembership.exception.MembershipException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;
    @Mock
    private MembershipRepository membershipRepository;
    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @Test
    void 멤버십등록실패_이미존재함() {
        // given
        Mockito.doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    void 멤버십등록성공() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        final MembershipResponse result = target.addMembership(userId, membershipType, point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
    }

    @Test
    void 멤버십목록조회() {
        // given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        // when
        final List<Membership> result = target.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
    }
}
