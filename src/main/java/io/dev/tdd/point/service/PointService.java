package io.dev.tdd.point.service;

import io.dev.tdd.point.PointHistory;
import io.dev.tdd.point.TransactionType;
import io.dev.tdd.point.UserPoint;
import io.dev.tdd.point.repository.impl.PointHistoryRepositoryImpl;
import io.dev.tdd.point.repository.impl.UserPointRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepositoryImpl pointHistoryRepository;
    private final UserPointRepositoryImpl userPointRepository;
    private final ConcurrentHashMap<Long, ReentrantLock> userMap = new ConcurrentHashMap<>();

    public UserPoint charge(long userId, long amount) {

        ReentrantLock lock = userMap.computeIfAbsent(userId, id -> new ReentrantLock());
        lock.lock();

        try {

            UserPoint resultUserPoint = userPointRepository.insertOrUpdate(userId, amount);
            pointHistoryRepository.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());


            return resultUserPoint;
        }finally {
            lock.unlock();
        }
    }

    public UserPoint use(long userId, long amount) {

        ReentrantLock lock = userMap.computeIfAbsent(userId, id -> new ReentrantLock());
        lock.lock();

        try {
            UserPoint resultUserPoint = userPointRepository.insertOrUpdate(userId, amount);
            pointHistoryRepository.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());

            return resultUserPoint;
        }finally {
            lock.unlock();
        }
    }


    public UserPoint selectByUserId(long userId) {
        return userPointRepository.selectByUserId(userId);
    }

    public List<PointHistory> selectAllByUserId(long userId) {
        return pointHistoryRepository.selectAllByUserId(userId);
    }


}
