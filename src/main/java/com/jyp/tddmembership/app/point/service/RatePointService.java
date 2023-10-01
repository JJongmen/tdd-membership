package com.jyp.tddmembership.app.point.service;

import org.springframework.stereotype.Service;

@Service
public class RatePointService implements PointService {

    public static final int POINT_RATE = 1;

    @Override
    public int calculateAmount(final int price) {
        return price * POINT_RATE / 100;
    }
}
