package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private com.itheima.interceptor.TokenInterceptor tokenInterceptor;

    @Autowired
    private ObjectMapper objectMapper;

    @org.junit.jupiter.api.BeforeEach
    void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testList() throws Exception {
        PageResult<Student> mockPage = new PageResult<>();
        mockPage.setTotal(5L);
        mockPage.setRows(Collections.emptyList());

        when(studentService.page(any(StudentQueryParam.class))).thenReturn(mockPage);

        mockMvc.perform(get("/students")
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.total").value(5));
    }

    @Test
    void testSave() throws Exception {
        Student student = new Student();
        student.setName("New Student");
        student.setNo("2023001");

        doNothing().when(studentService).save(any(Student.class));

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testDeleteStuById() throws Exception {
        List<Integer> ids = Arrays.asList(1, 2);
        doNothing().when(studentService).delete(ids);

        // Path variable list handling
        mockMvc.perform(delete("/students/{ids}", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testGetInfo() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setName("Test Student");

        when(studentService.getInfo(1)).thenReturn(student);

        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Student"));
    }

    @Test
    void testUpdate() throws Exception {
        Student student = new Student();
        student.setId(1);
        student.setName("Updated Student");

        doNothing().when(studentService).update(any(Student.class));

        mockMvc.perform(put("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }

    @Test
    void testUpdateViolation() throws Exception {
        doNothing().when(studentService).updateViolation(1, (short) 5);

        mockMvc.perform(put("/students/violation/{id}/{score}", 1, 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1));
    }
}
