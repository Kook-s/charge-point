package io.dev.tdd.point.service;

import io.dev.tdd.point.PointHistory;
import io.dev.tdd.point.PointValidator;
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
    private final PointValidator pointValidator;

    public UserPoint charge(long userId, long amount) {

        ReentrantLock lock = userMap.computeIfAbsent(userId, id -> new ReentrantLock());
        lock.lock();

        try {
            // Amount 검증
            long validChargeAmount = pointValidator.verifyChargeAmount(amount);

            // 포인트 변경
            long calculatedChangeAmount = userPointRepository.selectByUserId(userId).changePoint(validChargeAmount);

            // 포인트 최대치 검증
            long validChangeAmount = pointValidator.verifyChangePoint(calculatedChangeAmount);

            // 최종 검증 정보 업데이트
            UserPoint resultUserPoint = userPointRepository.insertOrUpdate(userId, validChangeAmount);

            // 히스토리 기록
            pointHistoryRepository.insert(userId, validChangeAmount, TransactionType.CHARGE, System.currentTimeMillis());

            return resultUserPoint;
        }finally {
            lock.unlock();
        }
    }

    public UserPoint use(long userId, long amount) {

        ReentrantLock lock = userMap.computeIfAbsent(userId, id -> new ReentrantLock());
        lock.lock();

        try {
            // 사용 포인트 검증
            long validUseAmount = pointValidator.verifyUseAmount(amount);

            // 포인트 변경
            long calculatedUseAmount = userPointRepository.selectByUserId(userId).usePoint(validUseAmount);

            // 포인트 최대치 검증
            long validUsePoint = pointValidator.verifyUsePoint(calculatedUseAmount);

            UserPoint resultUserPoint = userPointRepository.insertOrUpdate(userId, validUsePoint);
            pointHistoryRepository.insert(userId, validUsePoint, TransactionType.USE, System.currentTimeMillis());

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
