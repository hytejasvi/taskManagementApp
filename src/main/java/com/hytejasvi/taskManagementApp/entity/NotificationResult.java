package com.hytejasvi.taskManagementApp.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResult {

    private String mailId;
    private List<String> taskTitles;
}
