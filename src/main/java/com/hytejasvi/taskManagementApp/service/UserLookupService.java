package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLookupService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}

