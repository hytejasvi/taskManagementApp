package com.hytejasvi.taskManagementApp.controller;

import com.hytejasvi.taskManagementApp.api.response.QuotesApiResponse;
import com.hytejasvi.taskManagementApp.service.QuotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class UserController {

    @Autowired
    private QuotesService quotesService;

    @GetMapping("/server-test")
    public ResponseEntity<?> healthCheck() {
        QuotesApiResponse greetings = quotesService.getQuote();
        if (greetings != null) {
            return new ResponseEntity<>(greetings.getQuote(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Server is reachable", HttpStatus.OK);
        }
    }
}
