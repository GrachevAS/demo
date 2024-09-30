package com.example.demo.service;

import com.example.demo.exceptions.CustomException;
import com.example.demo.model.db.entity.User;
import com.example.demo.model.db.repository.UserRepository;
import com.example.demo.model.dto.request.UserInfoRequest;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserInfoResponse result = userService.createUser(request);
        assertEquals(user.getId(), result.getId());
    }
    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@.gmail.com");
        userService.createUser(request);
    }
    @Test(expected = CustomException.class)
    public void createUser_userExist() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
       userService.createUser(request);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        UserInfoResponse result = userService.getUser(user.getId());
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void updateUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.updateUser(user.getId(), request);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.UPDATED, user.getStatus());
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }
    @Test
    public void getAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);
        List<UserInfoResponse> result = userService.getAllUsers();
        assertEquals(users.size(), result.size());
    }

    @Test
    public void updateUserData() {
        User user = new User();
        user.setId(1L);
        userService.updateUserData(user);
        verify(userRepository, times(1)).save(any(User.class));
    }
}