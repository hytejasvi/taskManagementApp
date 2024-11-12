package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskServices {

    @Autowired
    private TaskEntryRepository taskEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public void createTask(String userName, Task task) {

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

        Task taskEntry = taskEntryRepository.save(task);
        User user = userService.findUserByUserName(userName);
        if (task.getCategory() == null) {
            task.setCategory("Personal");
        }
        user.getTasks().add(taskEntry);
        userService.saveUser(user);
        }

    public Task updateTask(Task task) {
        //write logic to get the task of this particular user / id and the update that.
        return null;
    }

    public List<Task> fetchAllTasksForUser(String userName) {
        User user = userRepository.findByUserName(userName);
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
        User user = userRepository.findByUserName(userName);
        if (user != null) {
            return user.getTasks().stream().filter
                    (s -> s.getId().equals(taskId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
