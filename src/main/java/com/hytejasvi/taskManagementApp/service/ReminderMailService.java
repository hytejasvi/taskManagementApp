package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import com.hytejasvi.taskManagementApp.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReminderMailService {

    @Autowired
    private TaskEntryRepository taskEntryRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 1800000)
    public void upcomingTasks() {
        List<User> users = userRepository.getUsersForMailNotification();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(90);
        ZonedDateTime zonedThreshold = threshold.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));
        Date thresholdDate = Date.from(zonedThreshold.toInstant());
        log.info("Threshold Date (UTC): {}", thresholdDate);
            Map<String, List<String>> mailerList = getMailerList(users, thresholdDate);

        String subject = "Task Deadline Reminder";
        for (Map.Entry<String, List<String>> entry : mailerList.entrySet()) {
            StringBuilder messageBody = new StringBuilder();
            for (String val : entry.getValue()) {
                messageBody.append(val + " /n");
            }
            log.info("mail details:");
            log.info("mailId: {}", entry.getKey());
            log.info("subject: {}", subject);
            log.info("mail body: {}", messageBody);
            emailService.sendMail(entry.getKey(), subject, messageBody.toString());
        }
    }

    private Map<String, List<String>> getMailerList(List<User> users, Date thresholdDate) {
        Map<String, List<String>> mailerList = new HashMap<>();
        for(User s: users) {
            List<Task> tasks = s.getTasks();
            List<String> titles = tasks.stream().filter(task -> task.getIsCompleted().equals(false))
                    .filter(task -> task.getDeadline().before(thresholdDate))
                    .map(Task::getTitle).toList();
            System.out.println("inside getMailerList: titles: "+titles);
            String mailId = s.getMailId();
            if (!titles.isEmpty()) {
                mailerList.put(mailId, titles);
            }
        }
        return mailerList;
    }
}
