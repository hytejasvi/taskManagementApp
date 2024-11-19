package com.hytejasvi.taskManagementApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hytejasvi.taskManagementApp.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskDTO {
    private String id; // Represent `id` as a string here
    private String title;
    private String description;
    private Boolean isCompleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy : HH-mm")
    private Date deadline;

    private String category;

    public TaskDTO(Task task) {
        this.id = task.getId().toString();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.isCompleted = task.getIsCompleted();
        this.deadline = task.getDeadline();
        this.category = task.getCategory();
    }
}
