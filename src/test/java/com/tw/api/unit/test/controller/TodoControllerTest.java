package com.tw.api.unit.test.controller;

import com.tw.api.unit.test.domain.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class TodoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    


}
