package io.dev.tdd.point;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {
    
    UserPoint userPoint;
    
    @BeforeEach
    void setup() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }
    
    @Test
    @DisplayName("기존 1000 포인트에 500 충전 포인트 요청 계산 후 1500 포인트 반영")
    public void changePoint() {
        //given
        long amount = 500L;

        //when
        long calculatedPoint = userPoint.changePoint(amount);

        //then
        assertThat(calculatedPoint).isEqualTo(1500L);
    }

    @Test
    @DisplayName("기존 1000 포인트에 500 사용 포인트 요청 계산 후 500 포인트 반영")
    public void userPoint() throws Exception {
        //given
        long amount = 500L;
        //when
        long calculatedPoint = userPoint.usePoint(amount);

        //then
        assertThat(calculatedPoint).isEqualTo(500L);
    }

}