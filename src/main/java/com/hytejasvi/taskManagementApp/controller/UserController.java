package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.api.response.QuotesApiResponse;
import com.hytejasvi.taskManagementApp.dto.UserDto;
import com.hytejasvi.taskManagementApp.service.QuotesService;
import com.hytejasvi.taskManagementApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping()
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.updateUser(userName, userDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<HttpStatus> deleteUserAccount() {
        userService.deleteUserAccount(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
