package io.dev.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long changePoint(long amount) {
        return this.point + amount;
    }

    public long usePoint(long amount) {
        return this.point - amount;
    }
}
