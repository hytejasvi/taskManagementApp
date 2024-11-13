package com.hytejasvi.taskManagementApp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private String userName;
    private String password;
    private String emailId;
    private List<String> roles;
}
