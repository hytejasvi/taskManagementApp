package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class TaskServices {

    @Autowired
    private TaskEntryRepository taskEntryRepository;


    public Task createTask(Task task) {
        //we will be adding the validations later
        /*Date deadline = task.getDeadline();
        Date currentDate = Date.from(Instant.now());
        try {
            if (deadline.before(currentDate)) {
                log.error("entered date / time is in past. Enter correct date / time");
                throw new Exception("");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        Task savedTask  = taskEntryRepository.save(task);
        return savedTask;
        }
    }
