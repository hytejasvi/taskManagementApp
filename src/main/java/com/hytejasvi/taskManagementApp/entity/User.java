package com.hytejasvi.taskManagementApp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private ObjectId userId;

    private String userName;
    private String password;

    @Indexed(unique = true)
    private String mailId;

    private List<String> roles;
}
