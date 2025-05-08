package io.dev.tdd.point.controller;

import io.dev.tdd.point.PointHistory;
import io.dev.tdd.point.UserPoint;
import io.dev.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    @GetMapping("/{id}")
    public UserPoint point(@PathVariable("id") long id) {

        return pointService.selectByUserId(id);
    }

    @GetMapping("/{id}/history")
    public List<PointHistory> history(@PathVariable("id") long id) {
        return pointService.selectAllByUserId(id);
    }

    @PatchMapping("/{id}/charge")
    public UserPoint charge(@PathVariable("id") long id, @RequestBody long amount) {
        return pointService.charge(id, amount);
    }

    @PatchMapping("/{id}/use")
    public UserPoint use(@PathVariable("id") long id, @RequestBody long amount) {
        return pointService.use(id, amount);
    }
}
