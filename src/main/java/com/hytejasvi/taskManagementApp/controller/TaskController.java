package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.service.TaskServices;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    public ResponseEntity<List<Task>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<Task> tasks = taskServices.fetchAllTasksForUser(userName);
        return new ResponseEntity<>(tasks, HttpStatus.ACCEPTED);
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask, @PathVariable ObjectId taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Task res = taskServices.updateTaskForUser(userName, taskId, updatedTask);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
