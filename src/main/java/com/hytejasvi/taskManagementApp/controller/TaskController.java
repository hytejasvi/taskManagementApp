package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.dto.TaskDTO;
import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.service.TaskServices;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {


    private static final Logger log = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private TaskServices taskServices;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        taskServices.createTask(authentication.getName(), task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        List<Task> tasks = taskServices.fetchAllTasksForUser(userName);
        List<TaskDTO> taskDTOs = tasks.stream().map(TaskDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(taskDTOs, HttpStatus.ACCEPTED);
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<Task> updateTask(@RequestBody Task updatedTask, @PathVariable ObjectId taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Task res = taskServices.updateTaskForUser(userName, taskId, updatedTask);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable ObjectId taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            taskServices.deleteTask(userName, taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
