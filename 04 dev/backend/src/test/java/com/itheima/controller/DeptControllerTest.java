package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeptService deptService;

    @MockBean
    private com.itheima.interceptor.TokenInterceptor tokenInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testList() throws Exception {
        // Mock data
        Dept d1 = new Dept();
        d1.setId(1);
        d1.setName("Research");
        Dept d2 = new Dept();
        d2.setId(2);
        d2.setName("Sales");
        List<Dept> mockDepts = Arrays.asList(d1, d2);

        when(deptService.findAll()).thenReturn(mockDepts);

        mockMvc.perform(get("/depts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1)) // Assuming Result.success() sets code to 1
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(deptService).deleteById(1);

        mockMvc.perform(delete("/depts").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testAdd() throws Exception {
        Dept dept = new Dept();
        dept.setName("New Dept");

        doNothing().when(deptService).add(any(Dept.class));

        mockMvc.perform(post("/depts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testGetInfo() throws Exception {
        Dept dept = new Dept();
        dept.setId(1);
        dept.setName("Research");

        when(deptService.getById(1)).thenReturn(dept);

        mockMvc.perform(get("/depts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.name").value("Research"));
    }

    @Test
    void testUpdate() throws Exception {
        Dept dept = new Dept();
        dept.setId(1);
        dept.setName("Updated Name");

        doNothing().when(deptService).update(any(Dept.class));

        mockMvc.perform(put("/depts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }
}
