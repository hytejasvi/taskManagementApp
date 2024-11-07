package com.hytejasvi.taskManagementApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class UserController {

    @GetMapping("/server-test")
    public String healthCheck() {
        return "Server is Running";
    }
}
