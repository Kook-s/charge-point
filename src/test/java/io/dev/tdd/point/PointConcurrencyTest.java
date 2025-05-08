package io.dev.tdd.point;

import io.dev.tdd.point.service.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Test
    public void concurrencyOneUserTest() {
        //given
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 4000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(1L, 2000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(1L, 4000L)).thenAccept(System.out::println),
                CompletableFuture.runAsync(() -> pointService.use(1L, 2000L))

        ).join();

        //when
        long resultPoint = pointService.selectByUserId(1L).point();

        //then
        pointService.selectAllByUserId(1L).forEach(System.out::println);
        assertThat(resultPoint).isEqualTo(4000-2000+4000-2000);
    }

    @Test
    public void concurrencyMultiUserTest() {
        // given
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> pointService.charge(2L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(3L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(2L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(3L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(2L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.charge(3L, 1000L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(2L, 500L)).thenAccept(System.out::println),
                CompletableFuture.supplyAsync(() -> pointService.use(3L, 500L)).thenAccept(System.out::println)
        ).join();

        // when
        long resultPoint01 = pointService.selectByUserId(2L).point();
        long resultPoint02 = pointService.selectByUserId(3L).point();

        // then
        pointService.selectAllByUserId(2L).forEach(System.out::println);
        pointService.selectAllByUserId(3L).forEach(System.out::println);
        assertThat(resultPoint01).isEqualTo( 1000 - 500 + 1000 - 500);
        assertThat(resultPoint02).isEqualTo( 1000 - 500 + 1000 - 500);
    }
}
