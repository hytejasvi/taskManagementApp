package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.service.TaskServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {


    @Autowired
    private TaskServices taskServices;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        taskServices.createTask(authentication.getName(), task);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
