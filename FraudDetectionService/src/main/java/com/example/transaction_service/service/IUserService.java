package com.example.transaction_service.service;

import com.example.transaction_service.dto.UserDto;
import com.example.transaction_service.entitie.User;

import java.util.List;

public interface IUserService {

    List<UserDto> getAllUser ();
    UserDto getUserById (Long id);
    UserDto createUser (User user);
    UserDto updateUser (UserDto userDto  , Long id);
    void deleteUser (Long id);
}
