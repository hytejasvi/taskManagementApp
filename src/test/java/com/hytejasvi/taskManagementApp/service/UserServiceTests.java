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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testPasswordEncodingDuringUserCreation() {
        userService.createUser(sampleUser);
        assertNotEquals("password", sampleUser.getPassword());
    }

    @Test
    public void testUserLoginSuccess() {
        // Mock UserDetails and set the username
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testUser");

        // Ensure the mock is returned when the service is called
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUserDetails);

        // Mock JWT generation
        when(jwtUtil.generateTokens("testUser")).thenReturn("jwtToken");

        // Initialize sampleUserDto with a valid username
        sampleUserDto = new UserDto();
        sampleUserDto.setUserName("testUser");

        // Call the method and verify the token
        String token = userService.userLogin(sampleUserDto);
        System.out.println("inside testUserLoginSuccess --> token: " + token);
        assertEquals("jwtToken", token);
    }

    @Test
    public void testUserLoginWithInvalidUsername() {
        when(userDetailsService.loadUserByUsername("invalidUser")).thenThrow(new UsernameNotFoundException("User not found"));
        assertThrows(IllegalArgumentException.class, () -> userService.userLogin(sampleUserDto));
    }

    @Test
    public void testFindUserByUserNameExists() {
        when(userRepository.findByUserName("testUser")).thenReturn(sampleUser);
        User foundUser = userService.findUserByUserName("testUser");
        assertNotNull(foundUser);
    }

    @Test
    public void testFindUserByUserNameNotExists() {
        when(userRepository.findByUserName("nonExistingUser")).thenReturn(null);
        User foundUser = userService.findUserByUserName("nonExistingUser");
        assertNull(foundUser);
    }

    @Test
    public void testSaveUser() {
        userService.saveUser(sampleUser);
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void testCreateAdminUser() {
        userService.createAdminUser(sampleUser);
        assertEquals(Arrays.asList("ADMIN", "user"), sampleUser.getRoles());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }


    @Test
    public void testUpdateUserWithNewData() {
        when(userRepository.findByUserName("testUser")).thenReturn(sampleUser);
        sampleUserDto.setUserName("updatedUser");
        sampleUserDto.setPassword("newPassword");

        userService.updateUser("testUser", sampleUserDto);
        assertEquals("updatedUser", sampleUser.getUserName());
        assertNotEquals("newPassword", sampleUser.getPassword()); // Password should be encoded
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    public void testUpdateUserWithEmptyFields() {
        when(userRepository.findByUserName("testUser")).thenReturn(sampleUser);
        sampleUserDto.setUserName(""); // Should not update
        userService.updateUser("testUser", sampleUserDto);
        assertEquals("testUser", sampleUser.getUserName()); // Username should remain the same
    }




}
