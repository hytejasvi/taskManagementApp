package com.hytejasvi.taskManagementApp.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "task_Entries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {

    @Id // this marks the variable as primary key
    private ObjectId id;

    @NonNull
    private String title;
    private String description;
    private Boolean isCompleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy : HH-mm")
    private Date deadline;

    private String category;
}
