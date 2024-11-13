package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.api.response.QuotesApiResponse;
import com.hytejasvi.taskManagementApp.dto.UserDto;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.service.QuotesService;
import com.hytejasvi.taskManagementApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private QuotesService quotesService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Signup failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUserName(),
                    userDto.getPassword()));
            return new ResponseEntity<>(userService.userLogin(userDto), HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("server-test")
    public ResponseEntity<?> healthCheck() {
        QuotesApiResponse greetings = quotesService.getQuote();
        if (greetings != null) {
            return new ResponseEntity<>(greetings.getQuote(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Server is reachable", HttpStatus.OK);
        }
    }
}
