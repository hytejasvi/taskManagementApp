package com.hytejasvi.taskManagementApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    private ObjectId userId;

    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Allow deserialization but restrict serialization
    private String password;

    @Indexed(unique = true)
    private String mailId;

    private List<String> roles;

    @DBRef
    private List<Task> tasks = new ArrayList<>();
}
