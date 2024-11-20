package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.repository.TaskEntryRepository;
import com.hytejasvi.taskManagementApp.repository.UserRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReminderMailService {

    @Autowired
    private TaskEntryRepository taskEntryRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void upcomingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(90);
        ZonedDateTime zonedThreshold = threshold.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"));
        Date thresholdDate = Date.from(zonedThreshold.toInstant());
        log.info("Threshold Date (UTC): {}", thresholdDate);
        Map<String, List<String>> mailerList = userRepository.getUsersForMailNotification(thresholdDate);
        log.info("inside upcomingTasks after query : "+mailerList.toString());
        String subject = "Task Deadline Reminder";
        for (Map.Entry<String, List<String>> entry : mailerList.entrySet()) {
            StringBuilder messageBody = new StringBuilder();
            for (String val : entry.getValue()) {
                messageBody.append(val+" /n");
            }
            log.info("mail details:");
            log.info("mailId: {}",entry.getKey());
            log.info("subject: {}", subject);
            log.info("mail body: {}", messageBody);
            emailService.sendMail(entry.getKey(), subject, messageBody.toString());
        }
    }
}
