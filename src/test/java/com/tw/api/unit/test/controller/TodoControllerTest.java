package com.tw.api.unit.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.server.handler.ExceptionHandlingWebHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    public void should_get_all_Todo() throws Exception{
        //given
        List<Todo> todoList = new ArrayList<>();
        Todo todo = new Todo(1, "Sample Test", false, 5);
        todoList.add(todo);
        when(todoRepository.getAll()).thenReturn(todoList);

        //when
        ResultActions resultActions = mvc.perform(get("/todos"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Sample Test")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[0].order", is(5)));
    }

    @Test
    public void should_get_Todo_by_Id() throws Exception{
        //given
        List<Todo> todoList = new ArrayList<>();
        Todo todo = new Todo(1, "Sample Test", false, 5);
        Todo newTodo = new Todo(2, "New Sample Test", false, 6);
        todoList.add(todo);
        todoList.add(newTodo);
        when(todoRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(todoList.get(1)));

        //when
        ResultActions resultActions = mvc.perform(get("/todos/2"));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("New Sample Test")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.order", is(6)));
    }

    @Test
    public void should_save_Todo() throws Exception{
        Todo todo = new Todo(1, "Sample Test", false, 5);
        todoRepository.add(todo);
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        //when
        ResultActions resultActions = mvc.perform(post("/todos").contentType("application/json").content(objectMapper.writeValueAsString(todo)));
        //then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sample Test"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.order").value(5));
    }

    @Test
    public void should_delete_Todo() throws Exception{
        //given
        Todo todo = new Todo(1, "Sample Test", false, 5);
        todoRepository.add(todo);
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        //when
        ResultActions resultActions = mvc.perform(delete("/todos/1"));

        //then
        resultActions.andExpect(status().isOk());

    }
}
