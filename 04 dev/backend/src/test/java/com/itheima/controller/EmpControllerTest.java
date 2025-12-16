package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpService empService;

    @MockBean
    private com.itheima.interceptor.TokenInterceptor tokenInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testPage() throws Exception {
        PageResult<Emp> mockPage = new PageResult<>();
        mockPage.setTotal(10L);
        mockPage.setRows(Collections.emptyList());

        when(empService.page(any(EmpQueryParam.class))).thenReturn(mockPage);

        mockMvc.perform(get("/emps")
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.total").value(10));
    }

    @Test
    void testSave() throws Exception {
        Emp emp = new Emp();
        emp.setUsername("testuser");
        emp.setName("Test User");
        emp.setEntryDate(LocalDate.now());

        doNothing().when(empService).save(any(Emp.class));

        mockMvc.perform(post("/emps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testDelete() throws Exception {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        doNothing().when(empService).delete(ids);

        mockMvc.perform(delete("/emps")
                .param("ids", "1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testGetInfo() throws Exception {
        Emp emp = new Emp();
        emp.setId(1);
        emp.setUsername("testuser");

        when(empService.getInfo(1)).thenReturn(emp);

        mockMvc.perform(get("/emps/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    void testUpdate() throws Exception {
        Emp emp = new Emp();
        emp.setId(1);
        emp.setUsername("updateduser");

        doNothing().when(empService).update(any(Emp.class));

        mockMvc.perform(put("/emps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }
}
