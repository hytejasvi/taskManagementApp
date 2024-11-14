package com.hytejasvi.taskManagementApp.service;

import com.hytejasvi.taskManagementApp.dto.UserDto;
import com.hytejasvi.taskManagementApp.entity.User;
import com.hytejasvi.taskManagementApp.repository.UserRepository;
import com.hytejasvi.taskManagementApp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    private User sampleUser;
    private UserDto sampleUserDto;

    @BeforeEach
    public void setup() {
        sampleUser = new User();
        sampleUser.setUserName("testUser");
        sampleUser.setPassword("password");
        sampleUser.setMailId("test@example.com");

        sampleUserDto = new UserDto();
        sampleUserDto.setUserName("testUser");
        sampleUserDto.setPassword("password");
        sampleUserDto.setEmailId("test@example.com");
    }

    @Test
    public void testCreateUserWithValidInput(){
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        userService.createUser(sampleUser);
        verify(userRepository, times(1)).save(sampleUser);

    }

    @Test
    public void testCreateUserWithEmptyUserName() {
        sampleUser.setUserName("");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(sampleUser));
    }
}
