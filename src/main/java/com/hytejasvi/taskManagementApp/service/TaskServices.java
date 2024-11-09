package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
