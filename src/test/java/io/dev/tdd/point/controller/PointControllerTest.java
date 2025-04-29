package io.dev.tdd.point.controller;

import io.dev.tdd.point.PointHistory;
import io.dev.tdd.point.UserPoint;
import io.dev.tdd.point.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(PointController.class)
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());
    }

    @Test
    public void testGetUserPoint() throws Exception {
        //given
        long id = 1L;
        given(pointService.selectByUserId(id)).willReturn(userPoint);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}", id));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

    @Test
    public void testGetUserHistory() throws Exception {
        //given
        long id = 1L;
        List<PointHistory> pointHistoryList = Collections.emptyList();
        given(pointService.selectAllByUserId(id)).willReturn(pointHistoryList);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}/history", id));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    
    @Test
    public void testCharge() throws Exception {
        //given
        long id = 1L;
        long amount = 500L;
        given(pointService.charge(id, amount)).willReturn(userPoint);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/charge", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    
    @Test
    public void testUse() throws Exception {
        //given
        long id = 1L;
        long amount = 500L;
        given(pointService.use(id, amount)).willReturn(userPoint);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/use", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }



















}