package io.dev.tdd.point.repository.impl;

import io.dev.tdd.point.UserPoint;
import io.dev.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointRepositoryImpl implements UserPointRepository {

    private final UserPointRepository userPointRepository;

    @Override
    public UserPoint selectByUserId(long id) {
        return userPointRepository.selectByUserId(id);
    }

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        return userPointRepository.insertOrUpdate(id, amount);
    }
}
