package com.jyp.tddmembership.app.point;

import org.springframework.stereotype.Service;

@Service
public class RatePointService {

    public static final int POINT_RATE = 1;

    public int calculateAmount(final int price) {
        return price * POINT_RATE / 100;
    }
}
