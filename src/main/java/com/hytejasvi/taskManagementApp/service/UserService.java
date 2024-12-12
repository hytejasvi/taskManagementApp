package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.dto.UserDto;
import com.hytejasvi.taskManagementApp.entity.Task;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import com.hytejasvi.taskManagementApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TaskServices taskServices;

    @Autowired
    private UserLookupService userLookupService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("Password encoded successfully");
            user.setRoles(List.of("user"));
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String userLogin(UserDto userDto) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUserName());
            String jwtToken = jwtUtil.generateTokens(userDetails.getUsername());
            log.info("get jwtToken successful");
            return jwtToken;
        } catch (Exception e) {
            throw new IllegalArgumentException (e);
        }
    }

    public User findUserByUserName(String userName) {
        return userLookupService.findUserByUserName(userName);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void createAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ADMIN", "user"));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(String currentUserName, UserDto userDto) {
        User currentUser = userLookupService.findUserByUserName(currentUserName);
        if (userDto.getUserName() != null && !userDto.getUserName().isEmpty()) {
            currentUser.setUserName(userDto.getUserName());
        }
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getEmailId() != null && !userDto.getEmailId().isEmpty()) {
            currentUser.setMailId(userDto.getEmailId());
        }
        userRepository.save(currentUser);
    }

    @Transactional
    public void deleteUserAccount(String userName) {
        User user = userLookupService.findUserByUserName(userName);

        // Delete tasks associated with the user
        taskServices.deleteTasks(user);
        log.info("user tasks deleted successfully");

        // Delete the user itself
        userRepository.delete(user);
        log.info("user removed successfully");
    }
}
