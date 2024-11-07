package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        try {
            if (user.getUserName().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (user.getMailId().isEmpty()) {
                throw new IllegalArgumentException("Mail ID cannot be empty");
            }
            user.setRoles(List.of("user"));
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
