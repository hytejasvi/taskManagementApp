package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TaskServices {

    @Autowired
    private TaskEntryRepository taskEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLookupService userLookupService;


    public void createTask(String userName, Task task) {

        //we will be adding the validations later
        Date deadline = task.getDeadline();
        log.info("current deadline is: {}", deadline);
        Date currentDate = Date.from(Instant.now());
        try {
            if (deadline.before(currentDate)) {
                log.error("entered date / time is in past. Enter correct date / time");
                throw new DateTimeException("entered date / time is in past. Enter correct date / time");
            }
        } catch (Exception e) {
            throw new DateTimeException("entered date / time is in past. Enter correct date / time");
        }

        Task taskEntry = taskEntryRepository.save(task);
        User user = userLookupService.findUserByUserName(userName);
        if (task.getCategory() == null) {
            task.setCategory("Personal");
        }
        user.getTasks().add(taskEntry);
        userRepository.save(user);
        }

    public Task updateTask(Task task) {
        //write logic to get the task of this particular user / id and the update that.
        return null;
    }

    public List<Task> fetchAllTasksForUser(String userName) {
        User user = userLookupService.findUserByUserName(userName);
        List<Task> tasks = new ArrayList<>();
        if (user != null) {
            tasks = user.getTasks();
        }
        return tasks;
    }

    public Task updateTaskForUser(String userName, ObjectId taskId, Task updatedTask) {
        Task task = findTaskForUser(userName, taskId);
        if (task != null) {
            if (updatedTask.getCategory() != null &&
                    !updatedTask.getCategory().equals(task.getCategory())) {
                task.setCategory(updatedTask.getCategory());
            }
            if (updatedTask.getDescription() != null &&
                    !updatedTask.getDescription().equals(task.getDescription())) {
                task.setDescription(updatedTask.getDescription());
            }
            if (!updatedTask.getTitle().equals(task.getTitle()) &&
                    !updatedTask.getTitle().isEmpty()) {
                task.setTitle(updatedTask.getTitle());
            }
            if (updatedTask.getIsCompleted() != null &&
                    !updatedTask.getIsCompleted().equals(task.getIsCompleted())) {
                task.setIsCompleted(updatedTask.getIsCompleted());
            }
            if (updatedTask.getDeadline() != null &&
                    !updatedTask.getDeadline().equals(task.getDeadline())) {
                task.setDeadline(updatedTask.getDeadline());
            }
            taskEntryRepository.save(task);
        }
        return task;
    }

    private Task findTaskForUser(String userName, ObjectId taskId) {
        User user = userLookupService.findUserByUserName(userName);
        if (user != null) {
            return user.getTasks().stream().filter
                    (s -> s.getId().equals(taskId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public void deleteTask(String userName, ObjectId taskId) {
        User user = userLookupService.findUserByUserName(userName);
        try {
            user.getTasks().removeIf(s -> s.getId().equals(taskId));
            userRepository.save(user);
            taskEntryRepository.deleteById(taskId);
        } catch (NullPointerException e) {
            throw new NullPointerException("User not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTasks(User user) {
        List<Task> tasks = user.getTasks();
        if (tasks != null && !tasks.isEmpty()) { // Check if tasks is not null before accessing
            List<ObjectId> taskIds = tasks.stream()
                    .map(Task::getId)
                    .toList(); // Efficiently map to task IDs
            taskEntryRepository.deleteAllById(taskIds); // Delete all tasks in batch
        }
    }
}
