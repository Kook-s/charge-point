package io.dev.tdd.point.service;

import io.dev.tdd.point.PointHistory;
import io.dev.tdd.point.PointValidator;
import io.dev.tdd.point.TransactionType;
import io.dev.tdd.point.UserPoint;
import io.dev.tdd.point.repository.PointHistoryRepository;
import io.dev.tdd.point.repository.UserPointRepository;
import io.dev.tdd.point.repository.impl.PointHistoryRepositoryImpl;
import io.dev.tdd.point.repository.impl.UserPointRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointHistoryRepositoryImpl pointHistoryRepository;
    @Mock
    private UserPointRepositoryImpl pointRepository;
    @Spy
    private PointValidator pointValidator;

    @InjectMocks
    private PointService pointService;

    private UserPoint userPoint;

    @BeforeEach
    public void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }


    @Test
    @DisplayName("[성공] 포인트 충전")
    public void chargeTest(){
        //given
        // 유저 기본 정보 주입
        when(pointRepository.selectByUserId(1L)).thenReturn(userPoint);
        // 포인트 1500 저장시 포인트 반환되도록 주입
        when(pointRepository.insertOrUpdate(1L, 1500L)).thenReturn(new UserPoint(1L, 1500L, System.currentTimeMillis()));

        //when
        //500 포인투 저장
        UserPoint result = pointService.charge(1L, 500L);

        //then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.point()).isEqualTo(1500L);
    }

    @Test
    @DisplayName("[실패] 포인트 충전")
    public void charge_error_Test(){
        // when & then
        assertThatThrownBy(() -> pointService.charge(1L, 0L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[성공] 포인트 사용")
    public void userTest() {
        //given
        when(pointRepository.selectByUserId(1L)).thenReturn(userPoint);
        when(pointRepository.insertOrUpdate(1L, 500L)).thenReturn(new UserPoint(1L, 500L, System.currentTimeMillis()));

        //when
        UserPoint result = pointService.use(1L, 500L);

        //then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.point()).isEqualTo(500L);
    }

    @Test
    @DisplayName("[실패] 포인트 사용")
    public void user_error_Test() {
        // when & then
        assertThatThrownBy(() -> pointService.use(1L, 10000L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("유저 포인트 조회")
    public void findUserPointTest() {
        //given
        when(pointRepository.selectByUserId(1L)).thenReturn(userPoint);

        //when
        UserPoint result = pointService.selectByUserId(1L);

        //then
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 히스토리 조회")
    public void findUserHistoryTest() {
        //given
        PointHistory pointHistory = new PointHistory(1L, 1L, 1000L, TransactionType.USE, System.currentTimeMillis());
        when(pointHistoryRepository.selectAllByUserId(1L)).thenReturn(Arrays.asList(pointHistory));

        //when
        List<PointHistory> result = pointService.selectAllByUserId(1L);

        //then
        assertThat(result.size()).isEqualTo(1);
    }


}






















