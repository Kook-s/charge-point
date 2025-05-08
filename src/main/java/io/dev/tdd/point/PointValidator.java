package io.dev.tdd.point;

import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    public long verifyChargeAmount(long amount) {

        if(amount <=  0) {
            throw new IllegalArgumentException("0 보다 큰 포인트를 충전해야 합니다.");
        }

        if(amount > 5000) {
            throw new IllegalArgumentException("1회 최대 충전한도는 5000포인트 입니다.");
        }

        return amount;
    }

    public long verifyUseAmount(long amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("0 포인트보다 큰 포인트를 사용해야 합니다");
        }

        if(amount > 5000) {
            throw new IllegalArgumentException("1회 최대 사용한도는 5000포인트 입니다.");
        }

        return amount;
    }

    public long verifyChangePoint(long amount) {
        if(amount > 10000) {
            throw new IllegalArgumentException("최대 1만 포인트까지만 보유할 수 있습니다.");
        }

        return amount;
    }

    public long verifyUsePoint(long amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("보유 포인트보다 많이 사용할 수 없습니다.");
        }
        return amount;
    }
}
